import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

// ui-model
import { UiFileChangeSetModel } from '../../../commonui/uimodel/ui-file-change-set-model';

@Component({
  selector: 'app-experimental-file-change-set-side-by-side-diff',
  templateUrl: './experimental-file-change-set-side-by-side-diff.component.html',
  styleUrls: ['./experimental-file-change-set-side-by-side-diff.component.css']
})
export class ExperimentalFileChangeSetSideBySideDiffComponent implements OnInit {

	public currentUiFileChangeSet: UiFileChangeSetModel = new UiFileChangeSetModel();	

	@Input() fileChangeSet:UiFileChangeSetModel = new UiFileChangeSetModel();

	constructor() { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		if(changes.fileChangeSet != undefined) {
			let fileChangeSetCandidate:UiFileChangeSetModel = changes.fileChangeSet.currentValue;
			this.currentUiFileChangeSet = fileChangeSetCandidate;
		}
	}

}
