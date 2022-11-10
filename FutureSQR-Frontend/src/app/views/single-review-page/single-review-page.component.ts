import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';


// Backend Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// Intermal Services
import { NavigationBarService } from '../../uiservices/navigation-bar.service';

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
		private navigationBarService : NavigationBarService,
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
			data => this.onReviewRevisionInformation(data),
			error => {}
		);
	}
	
	onReviewDataReceived(reviewData: BackendModelReviewData):  void {
		this.activeReviewData = reviewData;
		this.setNavigationInformation();
	}
	
	setNavigationInformation(): void {
		// Add navigation only after we received the review title as well.		
		let x = []
		// TODO: project display name - must retrieve info from service or backend. 
		x.push( this.navigationBarService.createItem( this.activeProjectID, ['/',this.activeProjectID], false ));
		// Provide extra reviews link
		x.push( this.navigationBarService.createItem( 'Reviews', ['/',this.activeProjectID,'reviews'], false ));
		// Provide title
		x.push( this.navigationBarService.createItem( this.activeReviewID+": "+this.activeReviewData.reviewTitle, ['/',this.activeProjectID, 'review', this.activeReviewID], true ));
		
		this.navigationBarService.setBreadcrumbNavigation(x);
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
		
		// TODO: reload unified diff data from server, depending on the current revision selection.
		// envdata should be a list of revisions enabled.
		// the single page has a list of revisions
		
		// we must encode the list as a single parameter
		// we must manipulate the parame parameter map, so in the link can be given a configuration
	}
	
	onProjectInformationProvided(projectinformation:any):void {
		
	}
	
	// open side by side dialog
	openSideBySideDialog( filechangeSet:BackendModelSingleCommitFileChangeSet ):void {
		const modalref = this.modalService.open(  SingleRevisionSideBySideDialogComponent,  {centered: true, ariaLabelledBy: 'modal-basic-title', size:<any>'fs'}    )
		
		modalref.componentInstance.setAllChangeSets(this.uiFileChangeSets);
		modalref.componentInstance.setSelectedFileChangeSet(filechangeSet);
		
		modalref.result.then((result) => {
			result.subscribe(
				data => {} ,
				error => {}
			)
		}, (reason) => {
		  // this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
	 	});
	}
	
	onReviewRevisionInformation(data): void {
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
			data => this.onReviewRevisionInformation(data),
			error => {}
		);
	}
	
}
