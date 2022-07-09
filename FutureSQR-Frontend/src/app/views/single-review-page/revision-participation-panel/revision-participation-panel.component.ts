import { Component, OnInit, Input, Output, SimpleChanges, EventEmitter } from '@angular/core';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';


import { AddRevisionToReviewSelectionDialogComponent } from '../../../commonui/add-revision-to-review-selection-dialog/add-revision-to-review-selection-dialog.component';

// should be a uimodel instead of a backend model
import { BackendModelReviewData } from '../../../backend/model/backend-model-review-data';


@Component({
  selector: 'app-revision-participation-panel',
  templateUrl: './revision-participation-panel.component.html',
  styleUrls: ['./revision-participation-panel.component.css']
})
export class RevisionParticipationPanelComponent implements OnInit {
	
	public currentUiReviewData: BackendModelReviewData = new BackendModelReviewData();
	
	@Input() activeReviewData: BackendModelReviewData = new BackendModelReviewData();
	@Output() onRevisionStateChanged = new EventEmitter<string>();

	constructor( private modalService: NgbModal) { }

	ngOnInit(): void {
	}
	
	ngOnChanges(changes: SimpleChanges): void {
		let reviewDataCandidate:BackendModelReviewData = changes.activeReviewData.currentValue;
		
		if(this.currentUiReviewData != reviewDataCandidate) {
			this.currentUiReviewData = reviewDataCandidate;
		}
	}
	

	openAddRevisionsDialog(reviewData:BackendModelReviewData): void {
		const modalref = this.modalService.open(  AddRevisionToReviewSelectionDialogComponent,  {centered: true, ariaLabelledBy: 'modal-basic-title', size:<any>'lg'}    )
		
		modalref.componentInstance.setActiveReviewData(this.currentUiReviewData);
		
		modalref.result.then((result) => {
			result.subscribe(
				data => {
					// TODO: onRevisionStateChanged.emit(updated....)))
					// this should trigger a reload of the revision data on the review page.
				} ,
				error => {}
			)
		}, (resason) => {
			
		})	
		
	}
}
