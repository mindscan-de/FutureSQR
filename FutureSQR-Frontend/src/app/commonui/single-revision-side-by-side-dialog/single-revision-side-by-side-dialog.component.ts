import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

// Should be a ui model instead of a backend model...
import { BackendModelSingleCommitFileChangeSet } from '../../backend/model/backend-model-single-commit-file-change-set';


@Component({
  selector: 'app-single-revision-side-by-side-dialog',
  templateUrl: './single-revision-side-by-side-dialog.component.html',
  styleUrls: ['./single-revision-side-by-side-dialog.component.css']
})
export class SingleRevisionSideBySideDialogComponent implements OnInit {
	
	private allUiFileChangeSets: BackendModelSingleCommitFileChangeSet[] = [];
	public currentUiFileChangeSet: BackendModelSingleCommitFileChangeSet = new BackendModelSingleCommitFileChangeSet();
	
	constructor(public activeModal: NgbActiveModal) { }
	
	ngOnInit() : void { }
	
	onClose() : void  {
		this.activeModal.close(null)
	}
	
	setAllChangeSets(allFileChangeSets:BackendModelSingleCommitFileChangeSet[]): void {
		this.allUiFileChangeSets = allFileChangeSets;
	}
	
	setSelectedFileChangeSet(newFileChangeSet:BackendModelSingleCommitFileChangeSet) : void {
		this.currentUiFileChangeSet = newFileChangeSet;
	}
	
	

	ngOnChanges(changes: SimpleChanges): void {
		let fileChangeSetCandidate:BackendModelSingleCommitFileChangeSet = changes.fileChangeSet.currentValue;
		
		if(this.currentUiFileChangeSet != fileChangeSetCandidate) {
			this.currentUiFileChangeSet = fileChangeSetCandidate;
		}
	}


}
