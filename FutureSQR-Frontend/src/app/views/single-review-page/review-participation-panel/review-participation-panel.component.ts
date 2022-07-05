import { Component, OnInit, Input, Output, SimpleChanges, EventEmitter, ChangeDetectorRef  } from '@angular/core';


// Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';


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
	@Output() onReviewStateChanged = new EventEmitter<string>();

	constructor( private projectDataQueryBackend : ProjectDataQueryBackendService, private cdr: ChangeDetectorRef  ) { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		let reviewDataCandidate:BackendModelReviewData = changes.activeReviewData.currentValue;
		
		if(this.currentUiReviewData != reviewDataCandidate) {
			this.currentUiReviewData = reviewDataCandidate;
			this.currentReviewers = new Map<string, BackendModelReviewResult>(Object.entries(reviewDataCandidate.reviewReviewersResults));
			this.isCurrentUserAReviewer = this.currentReviewers.has('mindscan-de');
			this.cdr.detectChanges();
		}
	}
	
	onCloseReview(projectid:string, reviewId:string):void {
		// use the backend service to close this review.
		this.projectDataQueryBackend.closeReview(projectid, reviewId).subscribe(
			data => {
				// parent component should reload the page...
				this.onReviewStateChanged.emit('close');
			},
			error => {}			
		);
	}
	
	onReopenReview(projectid:string, reviewId:string):void {
		// use the backend service to close this review.
		this.projectDataQueryBackend.reopenReview(projectid, reviewId).subscribe(
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
		// use the abckend service to add me to review
		this.projectDataQueryBackend.addReviewer(projectid, reviewId, 'mindscan-de').subscribe(
			data => {
				this.onReviewStateChanged.emit('revieweradded');
			},
			error => {}
		);
	}
	
	onReviewApprove(projectid:string, reviewId:string): void {
		this.projectDataQueryBackend.approveReview(projectid, reviewId, 'mindscan-de').subscribe(
			data => {
				this.onReviewStateChanged.emit('reviewapproved');
			},
			error => {}
		);
	}
	
	onReviewConcern(projectid:string, reviewId:string): void {
		this.projectDataQueryBackend.concernReview(projectid, reviewId, 'mindscan-de').subscribe(
			data => {
				this.onReviewStateChanged.emit('reviewapproved');
			},
			error => {}
		);
	}

}
