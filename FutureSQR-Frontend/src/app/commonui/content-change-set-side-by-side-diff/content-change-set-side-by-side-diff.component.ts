import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

import { TransformChangeSet } from '../../m2m/transform-change-set';

// ui model
import { UiContentChangeSetModel  } from '../uimodel/ui-content-change-set-model';
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

	@Input() contentChangeSet:UiContentChangeSetModel = new UiContentChangeSetModel([],1,1);
	

	constructor() { }

	ngOnInit(): void {
	}
	
 	ngOnChanges(changes: SimpleChanges): void {
	
		if(changes.contentChangeSet) {
			let currentCCS:UiContentChangeSetModel = changes.contentChangeSet.currentValue;
			
			this.leftContent = TransformChangeSet.fromUiContentChangeSetToSingleSideDiffContent(currentCCS, UiSingleSideEnum.Left);
			this.rightContent = TransformChangeSet.fromUiContentChangeSetToSingleSideDiffContent(currentCCS, UiSingleSideEnum.Right);
		}
	}

}
