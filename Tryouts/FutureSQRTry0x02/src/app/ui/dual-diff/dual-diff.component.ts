import { Component, OnInit } from '@angular/core';

// ui model
import { UiDiffContentModel } from './uimodel/ui-diff-content-model';

@Component({
  selector: 'app-dual-diff',
  templateUrl: './dual-diff.component.html',
  styleUrls: ['./dual-diff.component.css']
})
export class DualDiffComponent implements OnInit {
	
	public leftContent : UiDiffContentModel = new UiDiffContentModel("",1); 
	public rightContent : UiDiffContentModel = new UiDiffContentModel("",1);
	
	public readOnly:boolean = true;	
	

  constructor() { }

  ngOnInit(): void {
  }

	setDiffContentChangeSet() : void {
		// this.leftContent = this.filterLeftDiff(this.lineDiffData);
		// this.rightContent = this.filterRightDiff(this.lineDiffData)
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
