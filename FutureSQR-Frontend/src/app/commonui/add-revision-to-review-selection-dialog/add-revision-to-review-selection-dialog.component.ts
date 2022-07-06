import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';


// should be a uimodel instead of a backend model
import { BackendModelReviewData } from '../../backend/model/backend-model-review-data';
import { BackendModelProjectRecentCommits } from '../../backend/model/backend-model-project-recent-commits';


@Component({
  selector: 'app-add-revision-to-review-selection-dialog',
  templateUrl: './add-revision-to-review-selection-dialog.component.html',
  styleUrls: ['./add-revision-to-review-selection-dialog.component.css']
})
export class AddRevisionToReviewSelectionDialogComponent implements OnInit {

	public uiModelRecentProjectCommitsGroupedByDate: Map<string, BackendModelProjectRecentCommits> = new Map<string, BackendModelProjectRecentCommits>();
	public currentUiReviewData : BackendModelReviewData = new BackendModelReviewData();

	constructor(public activeModal: NgbActiveModal) { }

	ngOnInit(): void {
	}

	setActiveReviewData(activeReviewData: BackendModelReviewData): void {
		this.currentUiReviewData = activeReviewData;
		
		// TODO collect the project commits groupted by date, without those
		//      which are already connected to reviews.
	}
}
