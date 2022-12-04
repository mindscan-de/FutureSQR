import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

import { TransformChangeSet } from '../../m2m/transform-change-set';

// ui model
import { UiContentChangeSetModel  } from '../uimodel/ui-content-change-set-model';
import { UiUnifiedDiffContentModel } from '../uimodel/ui-unified-diff-content-model';


@Component({
  selector: 'app-content-change-set-single-diff',
  templateUrl: './content-change-set-single-diff.component.html',
  styleUrls: ['./content-change-set-single-diff.component.css']
})
export class ContentChangeSetSingleDiffComponent implements OnInit {
	
	// diff content to show
	public diffContent : UiUnifiedDiffContentModel = new UiUnifiedDiffContentModel("",1,1);
	
	// make the editor readonly
	public readOnly:boolean = true;
	public viewPortMargin:number = 1;
	
	@Input() contentChangeSet:UiContentChangeSetModel = new UiContentChangeSetModel([],1,0,1,0);

	constructor() { }

	ngOnInit(): void {
	}

	// maybe we don't need the update thing but only the setting this value once...
 	ngOnChanges(changes: SimpleChanges): void {
		if(changes.contentChangeSet) {
			let currentCCS:UiContentChangeSetModel = changes.contentChangeSet.currentValue;

			this.diffContent = TransformChangeSet.fromUiContentChangeSetToUnifiedDiffContent(currentCCS);
			this.viewPortMargin = Math.min(Math.max(currentCCS.diffContent.length,1),30);
		}
	}

}
