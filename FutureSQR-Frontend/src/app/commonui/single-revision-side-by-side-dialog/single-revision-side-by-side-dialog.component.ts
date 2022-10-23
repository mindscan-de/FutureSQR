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
	private allUiFileChangeSetsProvided: boolean = false;
	public  currentUiFileChangeSet: BackendModelSingleCommitFileChangeSet = new BackendModelSingleCommitFileChangeSet();
	private currentUiFileChangeSetProvided: boolean = false;
	public numberOfFiles: number = 0;
	private currentShownFileIndex = 0;
	public currentShownFileUiIndex = 0;
	
	constructor(public activeModal: NgbActiveModal) { }
	
	ngOnInit() : void { }
	
	onClose() : void  {
		this.activeModal.close(null)
	}
	
	setAllChangeSets(allFileChangeSets:BackendModelSingleCommitFileChangeSet[]): void {
		this.numberOfFiles = allFileChangeSets.length;
		this.allUiFileChangeSets = allFileChangeSets;
		this.allUiFileChangeSetsProvided = true;
		this.updateShownFileIndex();
	}
	
	setSelectedFileChangeSet(newFileChangeSet:BackendModelSingleCommitFileChangeSet) : void {
		this.currentUiFileChangeSet = newFileChangeSet;
		this.currentUiFileChangeSetProvided = true;
		this.updateShownFileIndex();
	}
	
	private updateShownFileIndex() : void {
		if(!this.allUiFileChangeSetsProvided) {
			return;
		}
		if(!this.currentUiFileChangeSetProvided) {
			return;
		}
		
		this.currentShownFileIndex = 0;
		this.currentShownFileUiIndex = 0;
		for(let i:number=0;i<this.allUiFileChangeSets.length;i++) {
			if(this.allUiFileChangeSets[i]==this.currentUiFileChangeSet) {
				this.currentShownFileIndex = i;
				this.currentShownFileUiIndex = i+1;
				return;
			}
		}
	}

	ngOnChanges(changes: SimpleChanges): void {
		let fileChangeSetCandidate:BackendModelSingleCommitFileChangeSet = changes.fileChangeSet.currentValue;
		
		if(this.currentUiFileChangeSet != fileChangeSetCandidate) {
			this.currentUiFileChangeSet = fileChangeSetCandidate;
		}
	}


}
