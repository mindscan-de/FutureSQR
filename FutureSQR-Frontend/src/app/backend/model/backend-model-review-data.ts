import { BackendModelReviewResult } from './backend-model-review-result';

export class BackendModelReviewData {
	public reviewId:string = "";
	public reviewTitle:string = "";
	public reviewLifecycleState:string = "";
	public reviewDescription: string = "";
	public reviewRevisions: string[] = [];
	// 
	public reviewAuthors: string[] = [];
	
	public reviewReviewersResults: Map<string,BackendModelReviewResult> = new Map<string, BackendModelReviewResult>();
	public reviewReadyToClose: boolean = false;
	
	public reviewFkProjectId: string = "";
}
