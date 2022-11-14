import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

// Backend Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';


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
	public revisionChangedCallbackFkt: () => void = () => {};

	constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,
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
	
	setRevisionConfigurationChangedCallback(fkt: () => void): void {
		this.revisionChangedCallbackFkt = fkt;
	}
	
	
	removeRevisionFromReview(projectid, reviewid, revisonid): void {
		
	}

}
