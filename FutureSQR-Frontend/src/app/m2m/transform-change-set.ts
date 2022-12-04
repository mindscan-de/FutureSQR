
// Backend
import { BackendModelSingleCommitFileChangeSet } from '../backend/model/backend-model-single-commit-file-change-set';
import { BackendModelSingleCommitFileContentChangeSet } from '../backend/model/backend-model-single-commit-file-content-change-set';

// Frontend
import { UiFileChangeSetModel } from '../commonui/uimodel/ui-file-change-set-model';
import { UiContentChangeSetModel } from '../commonui/uimodel/ui-content-change-set-model';
import { UiSingleSideDiffContentModel, UiSingleSideEnum } from '../commonui/uimodel/ui-single-side-diff-content-model';

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
		converted.setDiffLine( backendModel.lazy_diff_line );
		
		// TODO: convert filemode ets fromIndex / toIndex
		converted.setIndexLine( backendModel.lazy_index_line );
		
		return converted;
	}
	
	public static fromBackendFileToUiContentChangeSetModel(
						backendModel: BackendModelSingleCommitFileContentChangeSet ): UiContentChangeSetModel {
		let diffContent = backendModel.line_diff_data;

		// Well - not exceptionally beautiful..... Extract the line numbers from the backend Model.
		let lineInfoSplitted:string[] = backendModel.line_info.split("@@");
		let linedata_splitted:string[] = lineInfoSplitted[1].trim().split(/[,+\-]/u);
		 
		let leftLineStart:number = +linedata_splitted[1];
		let rightLineStart:number = +linedata_splitted[3];
		
		// TODO: left length = linedata_splitted[2]
		// TODO: right length = linedata_splitted[4]
		
		return new UiContentChangeSetModel(  diffContent, leftLineStart, rightLineStart );
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
	
}
