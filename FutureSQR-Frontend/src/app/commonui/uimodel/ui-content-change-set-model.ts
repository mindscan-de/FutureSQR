export class UiContentChangeSetModel {
	
	// each line starts with a " ", "-" or "+" according to GIT based diffs.
	// 
	public diffContent: string[] = [];
	public diffLeftLineCountStart:number = 1;
	public diffRightLineCountStart:number = 1;
	
	constructor(content:string[], startLeft:number, startRight:number) {
		this.diffContent = content;
		this.diffLeftLineCountStart = startLeft;
		this.diffRightLineCountStart = startRight;
	}
	
	getLineCountStartLeft() : number {
		return this.diffLeftLineCountStart;
	}
	
	getLineCountStartRight() : number {
		return this.diffRightLineCountStart;
	}
	
	getDiffContent() : string[] {
		return this.diffContent;
	}
	
}
