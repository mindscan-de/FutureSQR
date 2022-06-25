import { BackendModelReviewData } from './backend-model-review-data';

export class BackendModelCreateReviewResult {
	public projectId: string = "";
	public revisionId: string = "";
	public reviewId:string = "";
	public reviewData: BackendModelReviewData = new BackendModelReviewData();
}
