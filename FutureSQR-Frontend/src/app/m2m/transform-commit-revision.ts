
// backend model
import { BackendModelSingleCommitFileActionsInfo } from '../backend/model/backend-model-single-commit-file-actions-info';


// UI Model
import { UiReviewFileInformation } from '../commonui/uimodel/ui-review-file-information';


export class TransformCommitRevision {
	
	public static convertToUiReviewFileinformationArray(fileChanges: BackendModelSingleCommitFileActionsInfo):  UiReviewFileInformation[] {
		let map = fileChanges.fileActionMap;
		
		let fileInformations : UiReviewFileInformation[] = [];
		for(let i: number = 0;i<map.length;i++) {
			let fileInfo: UiReviewFileInformation = new UiReviewFileInformation( map[i][1], map[i][0], true );
			fileInformations.push(fileInfo);
		}

		return fileInformations;		
	}
	
}
