export class UiDiffContentModel {
	public diffContent: string = "";
	public diffLineNumberStart: number = 1;
	
	constructor( content:string, start:number) {
		this.diffContent = content;
		this.diffLineNumberStart = start;
	}
}
