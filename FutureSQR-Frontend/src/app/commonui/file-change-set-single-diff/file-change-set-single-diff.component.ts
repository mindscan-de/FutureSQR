import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

import { TransformChangeSet } from '../../m2m/transform-change-set';

// Should be a ui model instead of a backend model...
import { BackendModelSingleCommitFileChangeSet } from '../../backend/model/backend-model-single-commit-file-change-set';

// ui model
import { UiFileChangeSetModel } from '../uimodel/ui-file-change-set-model';


@Component({
  selector: 'app-file-change-set-single-diff',
  templateUrl: './file-change-set-single-diff.component.html',
  styleUrls: ['./file-change-set-single-diff.component.css']
})
export class FileChangeSetSingleDiffComponent implements OnInit {
	
	public currentUiFileChangeSet: UiFileChangeSetModel = new UiFileChangeSetModel();

	// backed model is an intermediate modul until ui-model for a file changeset got refined.	
	@Input() fileChangeSet:BackendModelSingleCommitFileChangeSet = new BackendModelSingleCommitFileChangeSet();
	
	constructor() { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		if(changes.fileChangeSet) {
			let uiFileChangeSet:UiFileChangeSetModel = TransformChangeSet
															.fromBackendFileChangeSetToUiFileChangeSet(
																	changes.fileChangeSet.currentValue
																);
			
			if(this.currentUiFileChangeSet != uiFileChangeSet) {
				this.currentUiFileChangeSet = uiFileChangeSet;
			}
		}
	}
}
