import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

// ui model

import { UiSingleSideDiffContentModel, UiSingleSideEnum } from '../uimodel/ui-single-side-diff-content-model';


@Component({
  selector: 'app-content-change-set-side-by-side-diff',
  templateUrl: './content-change-set-side-by-side-diff.component.html',
  styleUrls: ['./content-change-set-side-by-side-diff.component.css']
})
export class ContentChangeSetSideBySideDiffComponent implements OnInit {
	
	public leftContent : UiSingleSideDiffContentModel = new UiSingleSideDiffContentModel("", 1, UiSingleSideEnum.Left); 
	public rightContent : UiSingleSideDiffContentModel = new UiSingleSideDiffContentModel("",1, UiSingleSideEnum.Right);
	
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
		let contentChangeSetCurrent:string[] = changes.contentChangeSet.currentValue;
		if(contentChangeSetCurrent) {
			
			// This needs to be reworked such that the line numbers are correctly transferred.
			this.leftContent = this.fromUnifiedDiffContentChangeSetToSingleSideDiffContent(contentChangeSetCurrent, 12, UiSingleSideEnum.Left);
			this.rightContent = this.fromUnifiedDiffContentChangeSetToSingleSideDiffContent(contentChangeSetCurrent, 15, UiSingleSideEnum.Right);
		}
	}

	fromUnifiedDiffContentChangeSetToSingleSideDiffContent(linediff:string[], line_count_start:number, side:UiSingleSideEnum ): UiSingleSideDiffContentModel {
		switch(side) {
			case UiSingleSideEnum.Left: {
				let leftdiff = linediff.filter(line => !line.startsWith("+")).join("\n");
				return new UiSingleSideDiffContentModel(leftdiff, line_count_start, side);
			}
			case UiSingleSideEnum.Right: {
				let rightdiff = linediff.filter(line => !line.startsWith("-")).join("\n");
				return new UiSingleSideDiffContentModel(rightdiff, line_count_start, side);
			} 
			case UiSingleSideEnum.Both: {
				throw new Error("Expected to be decided for one  side, wither left or right.");
			}
		}
	}

}
