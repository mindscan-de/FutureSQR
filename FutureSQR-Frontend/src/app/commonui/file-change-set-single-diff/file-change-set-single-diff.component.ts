import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

// ui model
import { UiFileChangeSetModel } from '../uimodel/ui-file-change-set-model';

@Component({
  selector: 'app-file-change-set-single-diff',
  templateUrl: './file-change-set-single-diff.component.html',
  styleUrls: ['./file-change-set-single-diff.component.css']
})
export class FileChangeSetSingleDiffComponent implements OnInit {
	
	public currentUiFileChangeSet: UiFileChangeSetModel = new UiFileChangeSetModel();

	@Input() fileChangeSet:UiFileChangeSetModel = new UiFileChangeSetModel();
	
	constructor() { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		if(changes.fileChangeSet) {
			let uiFileChangeSet:UiFileChangeSetModel = changes.fileChangeSet.currentValue;
			
			if(this.currentUiFileChangeSet != uiFileChangeSet) {
				this.currentUiFileChangeSet = uiFileChangeSet;
			}
		}
	}
}
