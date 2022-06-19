import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

// ui model
import { UiDiffContentModel } from './uimodel/ui-diff-content-model';


@Component({
  selector: 'app-content-change-set-single-diff',
  templateUrl: './content-change-set-single-diff.component.html',
  styleUrls: ['./content-change-set-single-diff.component.css']
})
export class ContentChangeSetSingleDiffComponent implements OnInit {
	// diff content to show
	public diffContent : UiDiffContentModel = new UiDiffContentModel("",1);
	
	// make the editor readonly
	public readOnly:boolean = true;
	public viewPortMargin:number = 1;
	
	// actually this will an intermediate external model
	@Input() contentChangeSet:string[] =[];

	constructor() { }

	ngOnInit(): void {
	}

	// maybe we don't need the update thing but only the setting this value once...
 	ngOnChanges(changes: SimpleChanges): void {
		let contentChangeSetCurrent:string[] = changes.contentChangeSet.currentValue;
		if(contentChangeSetCurrent) {
			this.viewPortMargin = Math.min(Math.max(contentChangeSetCurrent.length,1),30);
			this.diffContent = this.filterDiff(contentChangeSetCurrent);
		}
	}

	filterDiff(diffLines: string[]) : UiDiffContentModel {
		let diff = diffLines.join("\n");
		
		let result:UiDiffContentModel = new UiDiffContentModel(diff,12);
		
		return result; 
	}


}
