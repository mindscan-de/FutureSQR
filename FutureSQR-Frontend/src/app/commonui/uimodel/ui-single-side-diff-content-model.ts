/*
** Idea behind this Data Model is to describe the Content of one side of the diff.
*/
export enum UiSingleSideEnum {

	Left = 1,
	Right = 2,
	Both = Left|Right,
	
}

export class UiSingleSideDiffContentModel {
	
	// each line starts wit a " ", "-" or "+" according to GIT based diffs.
	// we will transform this to two arrays in the future, but for now, it 
	// does the job...
	
	public diffContent: string = "";
	public diffLineNumberStart:number = 1;
	public diffSide: UiSingleSideEnum = undefined;
	
	construcor( content: string, start:number, side:UiSingleSideEnum) {
		this.diffContent = content;
		this.diffLineNumberStart = start;
		this.diffSide = side;
	}
	
}

