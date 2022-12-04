export class UiContentChangeSetModel {
	
	// each line starts with a " ", "-" or "+" according to GIT based diffs.
	public diffContent: string[] = [];
	public diffLeftLineCountStart:number = 1;
	public diffLeftLineCountDelta:number = 0;
	public diffRightLineCountStart:number = 1;
	public diffRightLineCountDelta:number = 0;
	
	constructor(content:string[], startLeft:number, countLeft:number, startRight:number, countRight:number) {
		this.diffContent = content;
		this.diffLeftLineCountStart = startLeft;
		this.diffLeftLineCountDelta = countLeft;
		this.diffRightLineCountStart = startRight;
		this.diffRightLineCountDelta = countRight;
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
