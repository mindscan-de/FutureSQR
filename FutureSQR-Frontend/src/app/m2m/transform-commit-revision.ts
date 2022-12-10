
// backend model
import { BackendModelSingleCommitFileActionsInfo } from '../backend/model/backend-model-single-commit-file-actions-info';


// UI Model
import { UiReviewFileInformation } from '../commonui/uimodel/ui-review-file-information';


export class TransformCommitRevision {
	
	public static readonly FILE_INFO_FILEPATH : number  = 1;
	public static readonly FILE_INFO_FILEACTION: number = 0; 
	
	public static convertToUiReviewFileinformationArray(fileChanges: BackendModelSingleCommitFileActionsInfo):  UiReviewFileInformation[] {
		let map:string[][] = fileChanges.fileActionMap;
		
		let fileInformations : UiReviewFileInformation[] = [];
		for(let i: number = 0;i<map.length;i++) {
			let fileInfo: UiReviewFileInformation = new UiReviewFileInformation( 
															map[i][TransformCommitRevision.FILE_INFO_FILEPATH], 
															map[i][TransformCommitRevision.FILE_INFO_FILEACTION], 
															true );
						
			fileInformations.push(fileInfo);
		}

		return fileInformations;		
	}
	
}
