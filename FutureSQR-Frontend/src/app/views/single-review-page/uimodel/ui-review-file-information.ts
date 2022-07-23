export class UiReviewFileInformation {
	public fileAction: string = "";
	public fullFilePath: string = "";
	public simpleFileName: string = "";
	public parentFilePath: string = "";
	
	// maybe a separate object type
	// TODO: statistics
	// TODO: added lines
	// TOOD: removed lines
	
	// maybe a separate object type
	// TODO: open discussion threads open for this file
	// TOOD: resolved discussion threads for this file
	// TODO: number of discussion threads for this file
	
	constructor(filepath:string , fileAction: string ) {
		this.fileAction = fileAction;
		this.fullFilePath = filepath;
		
		//TODO: split the filepath into sinplefilename and parentfilepath
	}
}
