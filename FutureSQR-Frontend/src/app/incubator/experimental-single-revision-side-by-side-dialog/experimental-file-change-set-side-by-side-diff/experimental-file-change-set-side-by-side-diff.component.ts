import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

// Should be a ui model instead of a backend model...
import { BackendModelSingleCommitFileChangeSet } from '../../../backend/model/backend-model-single-commit-file-change-set';


@Component({
  selector: 'app-experimental-file-change-set-side-by-side-diff',
  templateUrl: './experimental-file-change-set-side-by-side-diff.component.html',
  styleUrls: ['./experimental-file-change-set-side-by-side-diff.component.css']
})
export class ExperimentalFileChangeSetSideBySideDiffComponent implements OnInit {

	public currentUiFileChangeSet: BackendModelSingleCommitFileChangeSet = new BackendModelSingleCommitFileChangeSet();	

	// backed model is an intermediate modul until ui-model for a file changeset got refined.	
	@Input() fileChangeSet:BackendModelSingleCommitFileChangeSet = new BackendModelSingleCommitFileChangeSet();

	constructor() { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		if(changes.fileChangeSet != undefined) {
			
			let fileChangeSetCandidate:BackendModelSingleCommitFileChangeSet = changes.fileChangeSet.currentValue;
			this.currentUiFileChangeSet = fileChangeSetCandidate;
		}
	}

}
