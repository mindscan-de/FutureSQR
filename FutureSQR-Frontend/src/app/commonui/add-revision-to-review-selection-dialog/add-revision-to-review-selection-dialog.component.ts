import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';


// should be a uimodel instead of a backend model
import { BackendModelReviewData } from '../../backend/model/backend-model-review-data';


@Component({
  selector: 'app-add-revision-to-review-selection-dialog',
  templateUrl: './add-revision-to-review-selection-dialog.component.html',
  styleUrls: ['./add-revision-to-review-selection-dialog.component.css']
})
export class AddRevisionToReviewSelectionDialogComponent implements OnInit {

	public currentUiReviewData : BackendModelReviewData = new BackendModelReviewData();

	constructor(public activeModal: NgbActiveModal) { }

	ngOnInit(): void {
	}

	setActiveReviewData(activeReviewData: BackendModelReviewData): void {
		this.currentUiReviewData = activeReviewData;
	}
}
