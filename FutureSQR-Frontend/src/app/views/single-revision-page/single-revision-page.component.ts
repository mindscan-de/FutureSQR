import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// BackendModel - should be actually a ui model 
import { BackendModelSingleCommitFullChangeSet } from '../../backend/model/backend-model-single-commit-full-change-set';
import { BackendModelSingleCommitFileChangeSet } from '../../backend/model/backend-model-single-commit-file-change-set';
import { BackendModelSingleCommitFileActionsInfo } from '../../backend/model/backend-model-single-commit-file-actions-info';
import { BackendModelProjectRecentCommitRevision } from '../../backend/model/backend-model-project-recent-commit-revision';


// Dialog 
import { SingleRevisionSideBySideDialogComponent } from '../../commonui/single-revision-side-by-side-dialog/single-revision-side-by-side-dialog.component';


@Component({
  selector: 'app-single-revision-page',
  templateUrl: './single-revision-page.component.html',
  styleUrls: ['./single-revision-page.component.css']
})
export class SingleRevisionPageComponent implements OnInit {
	
	public activeProjectID: string = '';
	public activeRevisionID: string = '';
    public uiModelSingleRevisionDiffs: BackendModelSingleCommitFullChangeSet = new BackendModelSingleCommitFullChangeSet();
	public uiFileChangeSets: BackendModelSingleCommitFileChangeSet[] = [];
	public uiFilePathActions: string[][] = [];
	public uiRevisionData: BackendModelProjectRecentCommitRevision = new BackendModelProjectRecentCommitRevision();

    constructor(private projectDataQueryBackend : ProjectDataQueryBackendService, private route: ActivatedRoute, private modalService: NgbModal ) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		this.activeRevisionID = this.route.snapshot.paramMap.get('revisionid');
	
		// TODO: query the revision change to previous revision from backend.
		this.projectDataQueryBackend.getRecentProjectRevisionDiffFullChangeSet(this.activeProjectID,this.activeRevisionID).subscribe(
			data => this.onSingeRevisionDiffProvided(data),
			error => console.log(error)
		);
		
		this.projectDataQueryBackend.getRecentProjectRevisionFilePathsData(this.activeProjectID,this.activeRevisionID).subscribe(
			data => this.onFileListActionsProvided(data),
			error => console.log(error)
		);
		
		// TOOD: maybe combine with  ""getRecentProjectRevisionDiffFullChangeSet""
		this.projectDataQueryBackend.getRecentProjectRevisionInformation(this.activeProjectID,this.activeRevisionID).subscribe(
			data => this.onRevisionInformationProvided(data),
			error => console.log(error)
		);
	}

	onSingeRevisionDiffProvided( diffData: BackendModelSingleCommitFullChangeSet):void {
		this.uiModelSingleRevisionDiffs = diffData;
		this.uiFileChangeSets = diffData.fileChangeSet;
	}
	
	onRevisionInformationProvided( revisionData: BackendModelProjectRecentCommitRevision): void {
		this.uiRevisionData = revisionData;
	}
	
	
	onFileListActionsProvided( fileChanges: BackendModelSingleCommitFileActionsInfo) : void {
		this.uiFilePathActions = fileChanges.fileActionMap;
	}

	// open side by side dialog
	openSideBySideDialog( filechangeSet ):void {
		
		const modalref = this.modalService.open(  SingleRevisionSideBySideDialogComponent,  {centered: true, ariaLabelledBy: 'modal-basic-title', size:<any>'fs'}    )
		
		modalref.componentInstance.setFileChangeSet(filechangeSet);
		// modalref.componentInstance.setBar
		
		modalref.result.then((result) => {
			result.subscribe(
				data => {} ,
				error => {}
			)
		}, (reason) => {
		  // this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
	 	});
		
	}

}
