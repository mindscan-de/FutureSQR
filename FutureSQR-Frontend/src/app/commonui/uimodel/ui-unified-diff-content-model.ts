export class UiUnifiedDiffContentModel {
	
	public diffContent: string = "";
	public diffLineNumberStartLeft: number = 1;
	public diffLineNumberStartRight: number = 1;
	
	constructor(content:string, leftStart:number, rightStart:number) {
		this.diffContent = content;
		this.diffLineNumberStartLeft = leftStart;
		this.diffLineNumberStartRight = rightStart;
	}
}
