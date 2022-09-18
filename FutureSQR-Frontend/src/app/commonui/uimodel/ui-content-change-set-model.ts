export class UiContentChangeSetModel {
	// the unified diff
	public unifiedDiffContent: string[] = [];
	
	// the staring line number of the version of the revision before change 
	public lineNumberStartLeft: number = 1;
	// the starting line number of the version of zhe revision after change
	public lineNumberStartRight: number = 1;
	
	constructor( unifiedDiffContent: string[], lineNumberStartLeft: number, lineNumberStartRight: number) {
		this.unifiedDiffContent =  unifiedDiffContent;
		this.lineNumberStartLeft = lineNumberStartLeft;
		this.lineNumberStartRight = lineNumberStartRight;
	}
}
