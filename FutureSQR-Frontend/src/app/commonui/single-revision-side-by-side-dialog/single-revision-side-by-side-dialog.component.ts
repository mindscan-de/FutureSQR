import { Component, Input, SimpleChanges } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

// Should be a ui model instead of a backend model...
import { BackendModelSingleCommitFileChangeSet } from '../../backend/model/backend-model-single-commit-file-change-set';


@Component({
  selector: 'app-single-revision-side-by-side-dialog',
  templateUrl: './single-revision-side-by-side-dialog.component.html',
  styleUrls: ['./single-revision-side-by-side-dialog.component.css']
})
export class SingleRevisionSideBySideDialogComponent  {
	
	public currentUiFileChangeSet: BackendModelSingleCommitFileChangeSet = new BackendModelSingleCommitFileChangeSet();
	
		// backed model is an intermediate modul until ui-model for a file changeset got refined.	
	@Input() fileChangeSet:BackendModelSingleCommitFileChangeSet = new BackendModelSingleCommitFileChangeSet();


	constructor(public activeModal: NgbActiveModal) { }
	
	onClose() : void  {
		this.activeModal.close(null)
	}

	ngOnChanges(changes: SimpleChanges): void {
		let fileChangeSetCandidate:BackendModelSingleCommitFileChangeSet = changes.fileChangeSet.currentValue;
		
		if(this.currentUiFileChangeSet != fileChangeSetCandidate) {
			this.currentUiFileChangeSet = fileChangeSetCandidate;
		}
	}


}
