import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

// Backend Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// Internal Ui Services
import { CurrentUserService } from '../../uiservices/current-user.service';


// should be a uimodel instead of a backend model
import { BackendModelReviewData } from '../../backend/model/backend-model-review-data';
import { BackendModelProjectRecentCommits } from '../../backend/model/backend-model-project-recent-commits';
import { BackendModelProjectRecentCommitRevision } from '../../backend/model/backend-model-project-recent-commit-revision';



@Component({
  selector: 'app-remove-revision-from-review-selection-dialog',
  templateUrl: './remove-revision-from-review-selection-dialog.component.html',
  styleUrls: ['./remove-revision-from-review-selection-dialog.component.css']
})
export class RemoveRevisionFromReviewSelectionDialogComponent implements OnInit {
	
	public currentUiReviewData : BackendModelReviewData = new BackendModelReviewData();
	public currentUiReviewRevisions: BackendModelProjectRecentCommitRevision[] = [];
	public revisionChangedCallbackFkt: () => void = () => {};

	constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private currentUserService: CurrentUserService,
		public activeModal: NgbActiveModal
	) { }

	ngOnInit(): void {
	}


	setActiveReviewData(activeReviewData: BackendModelReviewData): void {
		this.currentUiReviewData = activeReviewData;
		
		// TODO collect the project commits groupted by date, without those
		//      which are already connected to reviews.
		// console.log(activeReviewData);
		
		
		// TODO: collect current revisions and display these...
	}
	
	setUiReviewRevisions(reviewRevisions: BackendModelProjectRecentCommitRevision[]) : void {
		this.currentUiReviewRevisions = reviewRevisions;
	}
	
	setRevisionConfigurationChangedCallback(fkt: () => void): void {
		this.revisionChangedCallbackFkt = fkt;
	}
	
	removeRevision(revisionid:string):void {
		this.removeRevisionFromReview(
			this.currentUiReviewData.reviewFkProjectId,
			this.currentUiReviewData.reviewId,
			revisionid
		);
	}
	
	removeRevisionFromReview(projectid:string, reviewid:string , revisionid:string ): void {
		let uuid:string = this.currentUserService.getCurrentUserUUID();
		console.log("called");
		
		this.projectDataQueryBackend.removeRevisionFromReview(projectid, reviewid, revisionid, uuid).subscribe(
			data => {
				// file list was updated, we might want to remove the revision or gray out the button, for this particular revisionid.
				this.revisionChangedCallbackFkt();
			},
			error => {
				
			}
		);
	}

}
