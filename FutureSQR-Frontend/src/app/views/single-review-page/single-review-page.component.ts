import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';


// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// Backend Model
import { BackendModelReviewData } from '../../backend/model/backend-model-review-data';
import { BackendModelSingleCommitFullChangeSet } from '../../backend/model/backend-model-single-commit-full-change-set';
import { BackendModelSingleCommitFileChangeSet } from '../../backend/model/backend-model-single-commit-file-change-set';
import { BackendModelSingleCommitFileActionsInfo } from '../../backend/model/backend-model-single-commit-file-actions-info';
import { BackendModelProjectRecentCommitRevision } from '../../backend/model/backend-model-project-recent-commit-revision';

// UI Model
import { UiReviewFileInformation } from '../../commonui/uimodel/ui-review-file-information';


// TODO rework this with a review side by side dialog, which can work with a configured filechangeset and a file paging configuration
import { SingleRevisionSideBySideDialogComponent } from '../../commonui/single-revision-side-by-side-dialog/single-revision-side-by-side-dialog.component';

@Component({
  selector: 'app-single-review-page',
  templateUrl: './single-review-page.component.html',
  styleUrls: ['./single-review-page.component.css']
})
export class SingleReviewPageComponent implements OnInit {
	
	public activeProjectID: string = '';
	public activeReviewID: string = '';
	public uiFileInformations: UiReviewFileInformation[] = [];  

    public uiModelSingleRevisionDiffs: BackendModelSingleCommitFullChangeSet = new BackendModelSingleCommitFullChangeSet();
	public uiFileChangeSets: BackendModelSingleCommitFileChangeSet[] = [];
	public uiRevisionInformation: BackendModelProjectRecentCommitRevision[] = [];
	
	public activeReviewData: BackendModelReviewData = new BackendModelReviewData();

	constructor( 
		private projectDataQueryBackend : ProjectDataQueryBackendService, 
		private route: ActivatedRoute, 
		private modalService: NgbModal 
	) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		this.activeReviewID = this.route.snapshot.paramMap.get('reviewid');
		
		this.projectDataQueryBackend.getReviewData(this.activeProjectID, this.activeReviewID).subscribe(
			data => this.onReviewDataReceived(data),
			error => {}			
		);
		
		// query the filelist for this review
		this.projectDataQueryBackend.getReviewFilePathsData(this.activeProjectID,this.activeReviewID ).subscribe (
			data => this.onFileListActionsProvided(data),
			error => console.log(error)
		);

		this.projectDataQueryBackend.getReviewRevisionDiffFullChangeSet(this.activeProjectID,this.activeReviewID ).subscribe(
			data => this.onDiffDataReceived(data),
			error => {}
		);
		
		
		// query some revision information for this particular review
		this.projectDataQueryBackend.getReviewSimpleRevisionInformationList(this.activeProjectID,this.activeReviewID).subscribe(
			data => this.onReviewReviewInformation(data),
			error => {}
		);
	}
	
	onReviewDataReceived(reviewData: BackendModelReviewData):  void {
		this.activeReviewData = reviewData;
	}
	
	onDiffDataReceived( diffData: BackendModelSingleCommitFullChangeSet ):void {
		this.uiModelSingleRevisionDiffs = diffData;
		this.uiFileChangeSets = diffData.fileChangeSet;
	}

	onFileListActionsProvided( fileChanges: BackendModelSingleCommitFileActionsInfo) : void {
		let fileInformations : UiReviewFileInformation[] = [];
		
		let map = fileChanges.fileActionMap;
		for(let i: number = 0;i<map.length;i++) {
			let fileInfo: UiReviewFileInformation = new UiReviewFileInformation( map[i][1], map[i][0], true );
			fileInformations.push(fileInfo);
		}
		this.uiFileInformations = fileInformations;
	}
	
	onRevisionSelectionChanged(eventdata:any):void {
		console.log("Guess what");
		console.log(eventdata);
	}
	
	// open side by side dialog
	openSideBySideDialog( filechangeSet ):void {
		
		// actually we need another viewer? because of different change configuration
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
	
	onReviewReviewInformation(data): void {
		this.uiRevisionInformation = data;
	}
	
	reloadReviewInformation(event):void {
		this.projectDataQueryBackend.getReviewData(this.activeProjectID, this.activeReviewID).subscribe(
			data => this.onReviewDataReceived(data),
			error => {}			
		);
	}
	
	reloadRevisionInformation(event):void {
		console.log("We now should reload so much data....");
	
		// actually this method is called...
		
		// TODO: the first call after loading this page will somehow not update this page for some reason...
		// TODO: change detector? 
		 	
		this.projectDataQueryBackend.getReviewSimpleRevisionInformationList(this.activeProjectID,this.activeReviewID).subscribe(
			data => this.onReviewReviewInformation(data),
			error => {}
		);
	}
	
}
