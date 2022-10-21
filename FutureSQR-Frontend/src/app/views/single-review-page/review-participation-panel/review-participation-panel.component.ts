import { Component, OnInit, Input, Output, SimpleChanges, EventEmitter, ChangeDetectorRef  } from '@angular/core';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

// Used UI Components
import { AddParticipantToReviewSelectionDialogComponent } from '../../../commonui/add-participant-to-review-selection-dialog/add-participant-to-review-selection-dialog.component';
import { RemoveParticipantFromReviewSelectionDialogComponent } from '../../../commonui/remove-participant-from-review-selection-dialog/remove-participant-from-review-selection-dialog.component';

// Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';
import { UserDataQueryBackendService } from '../../../backend/services/user-data-query-backend.service';


// should be a uimodel instead of a backend model
import { BackendModelReviewData } from '../../../backend/model/backend-model-review-data';
import { BackendModelReviewResult } from '../../../backend/model/backend-model-review-result';

@Component({
  selector: 'app-review-participation-panel',
  templateUrl: './review-participation-panel.component.html',
  styleUrls: ['./review-participation-panel.component.css']
})
export class ReviewParticipationPanelComponent implements OnInit {
	
	public currentUiReviewData: BackendModelReviewData = new BackendModelReviewData();
	public currentReviewers: Map<string,BackendModelReviewResult> = new Map<string, BackendModelReviewResult>();
	public isCurrentUserAReviewer: Boolean = false;
	
	@Input() activeReviewData: BackendModelReviewData = new BackendModelReviewData();
	// maybe add the current selected reviewers, so these can be filtered?
	@Output() onReviewStateChanged = new EventEmitter<string>();

	constructor( 
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private userDataQueryBackend : UserDataQueryBackendService,
		private modalService: NgbModal,   
		private cdr: ChangeDetectorRef 
	) { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		let reviewDataCandidate:BackendModelReviewData = changes.activeReviewData.currentValue;
		let currentUserId = this.userDataQueryBackend.getCurrentUserUUID();
		
		if(this.currentUiReviewData != reviewDataCandidate) {
			this.currentUiReviewData = reviewDataCandidate;
			this.currentReviewers = new Map<string, BackendModelReviewResult>(Object.entries(reviewDataCandidate.reviewReviewersResults));
			this.isCurrentUserAReviewer = this.currentReviewers.has(currentUserId);
			this.cdr.detectChanges();
		}
	}
	
	onCloseReview(projectid:string, reviewId:string):void {
		let currentClosingUser:string = this.userDataQueryBackend.getCurrentUserUUID();
		
		// use the backend service to close this review.
		this.projectDataQueryBackend.closeReview(projectid, reviewId, currentClosingUser).subscribe(
			data => {
				// parent component should reload the page...
				this.onReviewStateChanged.emit('close');
			},
			error => {}			
		);
	}
	
	onReopenReview(projectid:string, reviewId:string):void {
		let currentReopeningUser:string = this.userDataQueryBackend.getCurrentUserUUID();
		
		// use the backend service to close this review.
		this.projectDataQueryBackend.reopenReview(projectid, reviewId, currentReopeningUser).subscribe(
			data => {
				this.onReviewStateChanged.emit('reopen');
			},
			error => {}			
		);
	}
	
	onDeleteReview(projectid:string, reviewId:string):void {
		// use the backend service to close this review.
		this.projectDataQueryBackend.deleteReview(projectid, reviewId).subscribe(
			data => {
				// parent component should reload the page...
				this.onReviewStateChanged.emit('delete');
			},
			error => {}			
		);
	}
	
	onAddMeToReview(projectid:string, reviewId:string): void {
		let currentUser:string = this.userDataQueryBackend.getCurrentUserUUID();
		
		// use the backend service to add me to review
		this.projectDataQueryBackend.addReviewer(projectid, reviewId, currentUser, currentUser).subscribe(
			data => {
				this.onReviewStateChanged.emit('revieweradded');
			},
			error => {}
		);
	}
	
	onAddReviewer(projectid:string, reviewId:string) : void {
		// open dialog and suggest user, by files or so or by search....
		// new planned dialog		
		const modalref = this.modalService.open( AddParticipantToReviewSelectionDialogComponent, {centered: true, ariaLabelledBy: 'modal-basic-title', size:<any>'lg'});
		
		let that = this;
		
		modalref.componentInstance.setActiveReviewData(this.currentUiReviewData);
/*		modalref.componentInstance.setParticipantConfigurationChangedCallback(
			function () {
				that.setParticipantConfigurationChanged();
			}
		);
*/		
		modalref.result.then((result)=> {
			// TODO: check this subscription, whether it shoule only be one
			result.subscribe(
				data => {},
				error => {}
			)
		},
		(reason)=>{});
		

		// This is some POC code, to show that adding a differernt user is doable 		
/*		let currentUser:string = this.userDataQueryBackend.getCurrentUserUUID();
		
		// THIS should be the result of a modal dialog selecting a reviewer.
		// but maybe this have to move to somewhere else....
		let selectedReviewer: string = "xxx"
		
		this.projectDataQueryBackend.addReviewer(projectid, reviewId, selectedReviewer, currentUser).subscribe(
			data => {
				this.onReviewStateChanged.emit('revieweradded');
			},
			error => {}
		);
*/	
	}
	
	onRemoveReviewer(projectid:string, reviewId:string) : void  {
		// TODO: actually if there is only one to remove, remove directly, 
		// without selection dialog otherwise present selection dialog
		
		if( this.currentUiReviewData.reviewReviewersResults == undefined || this.currentUiReviewData.reviewReviewersResults == null || this.currentUiReviewData.reviewReviewersResults.size==0) {
			return;
		}
		
		const reviewermap = new Map<string,BackendModelReviewResult>(Object.entries(this.currentUiReviewData.reviewReviewersResults));
		console.log(reviewermap);
		
		if(reviewermap.size==1) {
			let reviewer_uuid:string = reviewermap.entries().next().value[0];
			console.log(reviewer_uuid);
			
			let current_user_uuid:string = this.userDataQueryBackend.getCurrentUserUUID();
			
			this.projectDataQueryBackend.removeReviewer(
				this.activeReviewData.reviewFkProjectId, 
				this.activeReviewData.reviewId, 
				reviewer_uuid, 
				current_user_uuid
			).subscribe(
				data => {
					// TODO: reload der currentConfiguration
				},
				error => {}
			);
		}
		else {
			const modalref = this.modalService.open( RemoveParticipantFromReviewSelectionDialogComponent, {centered: true, ariaLabelledBy: 'modal-basic-title', size:<any>'lg'});
			
			let that = this;
			
			modalref.componentInstance.setActiveReviewData(this.currentUiReviewData);
			
			modalref.result.then((result)=> {
				// TODO: actually it is a promise...
				result.subscribe(
					data => {},
					error => {}
				)
			},
			(reason)=>{});
		}
	}
	
	setParticipantConfigurationChanged(): void {
		this.onReviewStateChanged.emit("participants changed.");
	}
	
	onReviewApprove(projectid:string, reviewId:string): void {
		let currentUser:string = this.userDataQueryBackend.getCurrentUserUUID();
		
		this.projectDataQueryBackend.approveReview(projectid, reviewId, currentUser).subscribe(
			data => {
				this.onReviewStateChanged.emit('reviewapproved');
			},
			error => {}
		);
	}
	
	onReviewConcern(projectid:string, reviewId:string): void {
		let currentUser:string = this.userDataQueryBackend.getCurrentUserUUID();
		
		this.projectDataQueryBackend.concernReview(projectid, reviewId, currentUser).subscribe(
			data => {
				this.onReviewStateChanged.emit('reviewapproved');
			},
			error => {}
		);
	}
	

}
