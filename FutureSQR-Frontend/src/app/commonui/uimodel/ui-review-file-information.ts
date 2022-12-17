export class UiReviewFileInformation {
	public fileAction: string = "";
	public fullFilePath: string = "";
	public simpleFileName: string = "";
	public fileExtension: string = "";
	public parentFilePath: string = "";
	
	public isFile: boolean = true;
	public isDirectory: boolean = false;
	
	// maybe a separate object type
	// TODO: statistics
	// TODO: added lines
	// TOOD: removed lines
	
	// maybe a separate object type
	// TODO: open discussion threads open for this file
	// TOOD: resolved discussion threads for this file
	// TODO: number of discussion threads for this file
	
	// maybe also a temporary file visited state - e.g. to highlight, a file which was not yet reviewed, as long as this page remains open. 
	// TODO: fileWasVisited
	
	// maybe also have children of type UiReviewFileInformation[];
	// TODO: implement also a filetree
	// TODO: implement a function to add a child
	// TODO: has child etc...
	
	constructor(filepath:string , fileAction: string, isFile: boolean ) {
		this.fileAction = fileAction;
		this.fullFilePath = filepath;
		
		let lastIndex = filepath.lastIndexOf('/');
		this.simpleFileName = filepath.slice(lastIndex+1);
		this.parentFilePath = "";
				
		if(lastIndex !== -1) {
			this.parentFilePath = filepath.slice(0,lastIndex);
		}
		
		let lastIndexExt = this.simpleFileName.lastIndexOf('.');
		this.fileExtension = this.simpleFileName.slice(lastIndexExt+1);
		
		this.isFile = isFile;
		this.isDirectory = !isFile;
	}
}
