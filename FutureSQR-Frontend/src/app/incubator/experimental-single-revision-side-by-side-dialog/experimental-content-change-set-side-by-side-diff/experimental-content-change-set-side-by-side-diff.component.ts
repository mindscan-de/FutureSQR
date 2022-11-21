import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-experimental-content-change-set-side-by-side-diff',
  templateUrl: './experimental-content-change-set-side-by-side-diff.component.html',
  styleUrls: ['./experimental-content-change-set-side-by-side-diff.component.css']
})
export class ExperimentalContentChangeSetSideBySideDiffComponent implements OnInit {
	
	public leftContent : ExperimentalUiDiffContentModel = new ExperimentalUiDiffContentModel("",1); 
	public rightContent : ExperimentalUiDiffContentModel = new ExperimentalUiDiffContentModel("",1);
	
	// make the editor readonly
	public readOnly:boolean = true;
	public viewPortMargin:number = 1;
	
	// TODO: create a ui model from it
	// actually this will an intermediate external model
	@Input() contentChangeSet:string[] =[];

	constructor() { }

	ngOnInit(): void {
	}
	
 	ngOnChanges(changes: SimpleChanges): void {
		if(changes.contentChangeSet != undefined) {
			let contentChangeSetCurrent:string[] = changes.contentChangeSet.currentValue;
			// This needs to be reworked such that the line numbers are correctly transferred.
			this.leftContent = this.filterLeftDiff(contentChangeSetCurrent, 12);
			this.rightContent = this.filterRightDiff(contentChangeSetCurrent, 15)
		}
	}
	
	private filterLeftDiff(linediff: string[], left_line_count_start: number) : ExperimentalUiDiffContentModel {
		let leftdiff = linediff.filter(line => !line.startsWith("+")).join("\n");
		
		let result:ExperimentalUiDiffContentModel = new ExperimentalUiDiffContentModel(leftdiff, left_line_count_start);
		
		return result; 
	}
	
	private filterRightDiff(linediff: string[], right_line_count_start: number) : ExperimentalUiDiffContentModel {
		let rightdiff = linediff.filter(line => !line.startsWith("-")).join("\n");
		
		let result:ExperimentalUiDiffContentModel = new ExperimentalUiDiffContentModel(rightdiff,right_line_count_start);
		
		return result; 
	}

	/**
	
	Algorithmic considerations:
	
	* No minus on the left side - only additions done on the right side (may still come from some other 
	  ContentChangeSet or File Changeset) -> mark each line as added
    * No plus on the right side - only deletions done on the left side (may still be to be found in other
      ContentChangesets or File changesets) -> mark each line as removed

	* each line should have a resolve state (Moved, Added, Deleted, Modified, WSOnly)
	* each line should have a source info (line, file) / multiple sources possible? / line range
	* each line should have a destination info (line, file) / multiple destinations possible? / line range
	
	* also try to resolve in blocks to reduce complexity
    
    * try to do a line by line match left/right using cooccurence matrix.
    * try to resolve order - maybe just some lines were moved around
	* first solve locally
	* then solve in file. report which parts could be matched
	* then resolve in commit. (e.g. extract method / move method to other class)
	* be whitespace resistant
	* calculate minimal diff between two matched lines - create left side / create right side highlights 
	* Try to explain the diff. e.g where which fragment comes from.
	* linesplits, line merges are interesting e.g. method arguments, method parameters,
	* inserts and split/merges
	* try solving misaligned "close brackets" - git-algorithm problem. - always the "wrong" brackets are aligned
      (git likely tried to solve the merge conflict errors)
	* try to figure out if only whitespace mismatch / e.g. code was indented / unindented
	* try to resolve one line and mark one line char by char as added/deleted/modified/unmodified/yet undecided
	* use own changes as source for test cases collect revisions for testcases 

	 */

}

export class ExperimentalUiDiffContentModel {
	public diffContent: string = "";
	public diffLineNumberStart: number = 1;
	
	constructor( content:string, start:number) {
		this.diffContent = content;
		this.diffLineNumberStart = start;
	}
}
