
// Backend
import { BackendModelSingleCommitFileChangeSet } from '../backend/model/backend-model-single-commit-file-change-set';
import { BackendModelSingleCommitFileContentChangeSet } from '../backend/model/backend-model-single-commit-file-content-change-set';

// Frontend
import { UiFileChangeSetModel } from '../commonui/uimodel/ui-file-change-set-model';
import { UiContentChangeSetModel } from '../commonui/uimodel/ui-content-change-set-model';

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
		
		// TODO: convert filenames fromFilename / toFilename
		backendModel.lazy_diff_line;
		// TODO: convert filemode ets fromIndex / toIndex
		backendModel.lazy_index_line;
		
		return converted;
	}
	
	public static fromBackendFileToUiContentChangeSetModel(
						backendModel: BackendModelSingleCommitFileContentChangeSet ): UiContentChangeSetModel {
		let diffContent = backendModel.line_diff_data;

		// TODO: extract the line numbers from the backend Model.
		let lineInfoSplitted:string[] = backendModel.line_info.split("@@");
		
		let leftLineStart = 12;
		let rightLineStart = 15;
		
		return new UiContentChangeSetModel(  diffContent, leftLineStart, rightLineStart );
	}
}
