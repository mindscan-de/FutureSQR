export class BackendModelReviewData {
	public reviewId:string = "";
	public reviewTitle:string = "";
	public reviewLifecycleState:string = "";
	public reviewDescription: string = "";
	public reviewRevisions: string[] = [];
	// 
	public reviewAuthors: Map<string,string> = new Map<string,string>();
	// TODO: we have review results by different reviewers, actually we want both here.
	public reviewReviewersResults: string[] = [];
	public reviewFkProjectId: string = "";
}
