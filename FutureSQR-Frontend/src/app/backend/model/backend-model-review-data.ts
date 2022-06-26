export class BackendModelReviewData {
	public reviewId:string = "";
	public reviewTitle:string = "";
	public reviewLifecycleState:string = "";
	public reviewDescription: string = "";
	public reviewRevisions: string[] = [];
	// 
	public reviewAuthors: string[] = [];
	public reviewFkProjectId: string = "";
}
