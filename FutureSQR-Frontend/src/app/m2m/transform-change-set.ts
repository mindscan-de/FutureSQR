
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
		
		converted.setScmFromPath(backendModel.fromPath);
		converted.setScmToPath(backendModel.toPath);
		
		converted.setFileAction(backendModel.fileAction);
		converted.setFileMode(backendModel.fileMode);
		converted.setFileParentRevision(backendModel.fileParentRevId);
		converted.setFileCurrentRevision(backendModel.fileCurrentRevId);
		
		return converted;
	}
	
	public static fromBackendFileToUiContentChangeSetModel(
						backendModel: BackendModelSingleCommitFileContentChangeSet ): UiContentChangeSetModel {
		let diffContent = backendModel.line_diff_data;
		
		let leftLineStart:number = backendModel.diffLeftLineCountStart;
		let leftLineCount:number = backendModel.diffLeftLineCountDelta;
		
		let rightLineStart:number = backendModel.diffRightLineCountStart;
		let rightLineCount:number = backendModel.diffRightLineCountDelta;
		
		return new UiContentChangeSetModel(  diffContent, leftLineStart, leftLineCount, rightLineStart, rightLineCount );
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
