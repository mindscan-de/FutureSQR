import { Component, OnInit, Input, SimpleChanges } from '@angular/core';


// Should be a ui model instead of a backend model...
import { BackendModelSingleCommitFileChangeSet } from '../../backend/model/backend-model-single-commit-file-change-set';


@Component({
  selector: 'app-file-change-set-single-diff',
  templateUrl: './file-change-set-single-diff.component.html',
  styleUrls: ['./file-change-set-single-diff.component.css']
})
export class FileChangeSetSingleDiffComponent implements OnInit {
	
	public currentUiFileChangeSet: BackendModelSingleCommitFileChangeSet = new BackendModelSingleCommitFileChangeSet();

	// backed model is an intermediate modul until ui-model for a file changeset got refined.	
	@Input() fileChangeSet:BackendModelSingleCommitFileChangeSet = new BackendModelSingleCommitFileChangeSet();
	
	constructor() { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		let fileChangeSetCandidate:BackendModelSingleCommitFileChangeSet = changes.fileChangeSet.currentValue;
		
		if(this.currentUiFileChangeSet != fileChangeSetCandidate) {
			this.currentUiFileChangeSet = fileChangeSetCandidate;
		}
	}
}
