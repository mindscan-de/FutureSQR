import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

// ui-model
import { UiFileChangeSetModel } from '../../commonui/uimodel/ui-file-change-set-model';


@Component({
  selector: 'app-experimental-single-revision-side-by-side-dialog',
  templateUrl: './experimental-single-revision-side-by-side-dialog.component.html',
  styleUrls: ['./experimental-single-revision-side-by-side-dialog.component.css']
})
export class ExperimentalSingleRevisionSideBySideDialogComponent implements OnInit {
	
	private allUiFileChangeSets: UiFileChangeSetModel[] = [];
	private allUiFileChangeSetsProvided: boolean = false;
	
	public  currentUiFileChangeSet: UiFileChangeSetModel = new UiFileChangeSetModel();
	private currentUiFileChangeSetProvided: boolean = false;
	
	public numberOfFiles: number = 0;
	private currentShownFileIndex = 0;
	public currentShownFileUiIndex = 0;
	public nextEnabled = false;
	public prevEnabled = false;
	

	constructor(
		public activeModal: NgbActiveModal
	) { }

	ngOnInit(): void {
 	}

	onClose() : void  {
		this.activeModal.close(null)
	}

	setAllChangeSets(allFileChangeSets:UiFileChangeSetModel[]): void {
		this.numberOfFiles = allFileChangeSets.length;
		this.allUiFileChangeSets = allFileChangeSets;
		this.allUiFileChangeSetsProvided = true;
		this.updateFileNavigationValues();
	}
	
	setSelectedFileChangeSet(newFileChangeSet:UiFileChangeSetModel) : void {
		this.currentUiFileChangeSet = newFileChangeSet;
		this.currentUiFileChangeSetProvided = true;
		this.updateFileNavigationValues();
	}
	
	onNavLeftClicked() : void {
		if( this.currentShownFileIndex <= 0) {
			return;
		}
		
		this.currentUiFileChangeSet = this.allUiFileChangeSets[this.currentShownFileIndex-1];
		
		this.updateFileNavigationValues();
	}
	
	onNavRightClicked() : void {
		if( this.currentShownFileIndex >= this.numberOfFiles -1) {
			return;
		}
		
		this.currentUiFileChangeSet = this.allUiFileChangeSets[this.currentShownFileIndex+1];
		
		this.updateFileNavigationValues();		
	}
	
	private updateFileNavigationValues() : void {
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
				
				this.nextEnabled = this.currentShownFileIndex < this.numberOfFiles-1;
				this.prevEnabled = this.currentShownFileIndex > 0;
				 
				return;
			}
		}
	}
}
