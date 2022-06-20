import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

// ui model
import { UiDiffContentModel } from './uimodel/ui-diff-content-model';


@Component({
  selector: 'app-content-change-set-side-by-side-diff',
  templateUrl: './content-change-set-side-by-side-diff.component.html',
  styleUrls: ['./content-change-set-side-by-side-diff.component.css']
})
export class ContentChangeSetSideBySideDiffComponent implements OnInit {
	
	public leftContent : UiDiffContentModel = new UiDiffContentModel("",1); 
	public rightContent : UiDiffContentModel = new UiDiffContentModel("",1);
	
	// make the editor readonly
	public readOnly:boolean = true;
	public viewPortMargin:number = 1;

	// actually this will an intermediate external model
	@Input() contentChangeSet:string[] =[];
	

	constructor() { }

	ngOnInit(): void {
	}
	
 	ngOnChanges(changes: SimpleChanges): void {
		let contentChangeSetCurrent = changes.contentChangeSet.currentValue;
		if(contentChangeSetCurrent) {
			this.leftContent = this.filterLeftDiff(contentChangeSetCurrent);
			this.rightContent = this.filterRightDiff(contentChangeSetCurrent)
		}
	}

	filterLeftDiff(linediff: string[]) : UiDiffContentModel {
		let leftdiff = linediff.filter(line => !line.startsWith("+")).join("\n");
		
		let result:UiDiffContentModel = new UiDiffContentModel(leftdiff,12);
		
		return result; 
	}
	
	filterRightDiff(linediff: string[]) : UiDiffContentModel {
		let rightdiff = linediff.filter(line => !line.startsWith("-")).join("\n");
		
		let result:UiDiffContentModel = new UiDiffContentModel(rightdiff,12);
		
		return result; 
	}

}
