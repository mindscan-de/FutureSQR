
// Backend
import { BackendModelSingleCommitFileChangeSet } from '../backend/model/backend-model-single-commit-file-change-set';
import { BackendModelSingleCommitFileContentChangeSet } from '../backend/model/backend-model-single-commit-file-content-change-set';

// Frontend
import { UiFileChangeSetModel } from '../commonui/uimodel/ui-file-change-set-model';
import { UiContentChangeSetModel } from '../commonui/uimodel/ui-content-change-set-model';
import { UiSingleSideDiffContentModel, UiSingleSideEnum } from '../commonui/uimodel/ui-single-side-diff-content-model';
import { UiUnifiedDiffContentModel } from '../commonui/uimodel/ui-unified-diff-content-model';

export class TransformChangeSet {
	
	public static fromBackendFileChangeSetToUiFileChangeSet( 
						backendModel: BackendModelSingleCommitFileChangeSet ): UiFileChangeSetModel {
		
		/*
		** We want to convert a BackendModel or FileCHangeSets
		*/
		let converted: UiFileChangeSetModel = new UiFileChangeSetModel();

		// convert the content change sets		
		converted.setUiContentChangeSet( 
								backendModel
								.fileContentChangeSet
								.map(
									(ccs) => TransformChangeSet.fromBackendFileToUiContentChangeSetModel(ccs)
								));
		
		// TODO: convert filenames fromFilename / toFilename / move or / renames / may also be cool to highlight.
		// actually this should be part of the scm part (server side, we just do this here to just make it work right now.)
		converted.setDiffLine( backendModel.lazy_diff_line );
		
		let [scmFromPath, scmToPath] = TransformChangeSet.scmGitCalculatePath(backendModel.lazy_diff_line);
		converted.setScmFromPath(scmFromPath);
		converted.setScmToPath(scmToPath);
		
		// TODO: convert filemode ets fromIndex / toIndex
		converted.setIndexLine( backendModel.lazy_index_line );
		
		return converted;
	}
	
	private static scmGitCalculatePath(diffline:string):[string,string] {
		if(diffline.startsWith("diff --git")) {
			let firstIndexASlash = diffline.indexOf(" a/");
			let lastIndexASlash = diffline.lastIndexOf(" a/");
			
			let firstIndexBSlash = diffline.indexOf(" b/");
			let lastIndexBSlash = diffline.lastIndexOf(" b/");

			let scmFromPath:string = diffline.substring(firstIndexASlash + 3, lastIndexBSlash);
			let scmToPath:string = diffline.substring(firstIndexBSlash+3);
			
			return [ scmFromPath, scmToPath ]
		}
		else {
			return ["",""];
		}
	}
	
	public static fromBackendFileToUiContentChangeSetModel(
						backendModel: BackendModelSingleCommitFileContentChangeSet ): UiContentChangeSetModel {
		let diffContent = backendModel.line_diff_data;
		
		if(backendModel.diffRightLineCountStart == -1 && backendModel.diffLeftLineCountStart== -1) {
			// TODO: remove this when we get rid of backendModel.line_info is removed.
			let lineInfoSplitted:string[] = backendModel.line_info.split("@@");
			let linedata_splitted:string[] = lineInfoSplitted[1].trim().split(/[,+\-]/u);
			 
			let leftLineStart:number = +linedata_splitted[1];
			let leftLineCount:number = +linedata_splitted[2];
			let rightLineStart:number = +linedata_splitted[3];
			let rightLineCount:number = +linedata_splitted[4];
			
			return new UiContentChangeSetModel(  diffContent, leftLineStart, leftLineCount, rightLineStart, rightLineCount );
		} else
		{
			let leftLineStart:number = backendModel.diffLeftLineCountStart;
			let leftLineCount:number = backendModel.diffLeftLineCountDelta;
			
			let rightLineStart:number = backendModel.diffRightLineCountStart;
			let rightLineCount:number = backendModel.diffRightLineCountDelta;
			
			return new UiContentChangeSetModel(  diffContent, leftLineStart, leftLineCount, rightLineStart, rightLineCount );
			
		}
	}
	
	public static fromUiContentChangeSetToSingleSideDiffContent(uiccs:UiContentChangeSetModel, side:UiSingleSideEnum ): UiSingleSideDiffContentModel {
		switch(side) {
			case UiSingleSideEnum.Left: {
				let leftdiff = uiccs.diffContent.filter(line => !line.startsWith("+")).join("\n");
				return new UiSingleSideDiffContentModel(leftdiff, uiccs.diffLeftLineCountStart, side);
			}
			case UiSingleSideEnum.Right: {
				let rightdiff = uiccs.diffContent.filter(line => !line.startsWith("-")).join("\n");
				return new UiSingleSideDiffContentModel(rightdiff, uiccs.diffRightLineCountStart, side);
			} 
			case UiSingleSideEnum.Both: {
				throw new Error("Expected to be decided for one  side, wither left or right.");
			}
		}
	}
	
	public static fromUiContentChangeSetToUnifiedDiffContent(uiccs:UiContentChangeSetModel): UiUnifiedDiffContentModel {
		let diff = uiccs.diffContent.join("\n");
		return new UiUnifiedDiffContentModel( diff, uiccs.getLineCountStartLeft(), uiccs.getLineCountStartRight());
	}
}
