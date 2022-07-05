import { BackendModelReviewResult } from './backend-model-review-result';

export class BackendModelReviewData {
	public reviewId:string = "";
	public reviewTitle:string = "";
	public reviewLifecycleState:string = "";
	public reviewDescription: string = "";
	public reviewRevisions: string[] = [];
	// 
	public reviewAuthors: string[] = [];
	// TODO: we have review results by different reviewers, actually we want both here.
	public reviewReviewersResults: Map<string,BackendModelReviewResult> = new Map<string, BackendModelReviewResult>();
	public reviewFkProjectId: string = "";
}
