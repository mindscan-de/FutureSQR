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

}

export class ExperimentalUiDiffContentModel {
	public diffContent: string = "";
	public diffLineNumberStart: number = 1;
	
	constructor( content:string, start:number) {
		this.diffContent = content;
		this.diffLineNumberStart = start;
	}
}
