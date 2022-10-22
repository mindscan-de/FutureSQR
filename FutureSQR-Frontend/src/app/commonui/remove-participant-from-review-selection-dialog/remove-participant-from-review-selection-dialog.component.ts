import { Component, OnInit } from '@angular/core';
import { KeyValue } from '@angular/common';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

// Backend Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// Internal Services
import { CurrentUserService } from '../../services/current-user.service';

// Backend Models
import { BackendModelReviewData } from '../../backend/model/backend-model-review-data';
import { BackendModelReviewResult } from '../../backend/model/backend-model-review-result';

@Component({
  selector: 'app-remove-participant-from-review-selection-dialog',
  templateUrl: './remove-participant-from-review-selection-dialog.component.html',
  styleUrls: ['./remove-participant-from-review-selection-dialog.component.css']
})
export class RemoveParticipantFromReviewSelectionDialogComponent implements OnInit {
	
	public currentUiReviewData : BackendModelReviewData = new BackendModelReviewData();
	public currentReviewers: Map<string, BackendModelReviewResult> = new Map<string, BackendModelReviewResult>();

	constructor(
		private projectDataQueryBackend: ProjectDataQueryBackendService,
		private currentUserService: CurrentUserService,
		public activeModal: NgbActiveModal
	) { }

	ngOnInit(): void {
		// we use the data 
	}

	removeParticipant(participant_uuid: string) : void {
		let current_user_uuid = this.currentUserService.getCurrentUserUUID();
		
		this.projectDataQueryBackend.removeReviewer(
			this.currentUiReviewData.reviewFkProjectId,
			this.currentUiReviewData.reviewId,
			participant_uuid,
			current_user_uuid
		).subscribe(
			data=> {
				// TODO: we actually want to exit this dialog, and inform, that a reviewer was added
			},
			error=> {}
		)
		
	}
	
	// Add some setters
	setActiveReviewData(activeReviewData: BackendModelReviewData): void {
		this.currentUiReviewData = activeReviewData;
		this.currentReviewers = new Map<string,BackendModelReviewResult>(Object.entries(this.currentUiReviewData.reviewReviewersResults));
	}
	
	// used for user map iteration. 	
	public originalOrder = (a: KeyValue<number,string>, b: KeyValue<number,string>): number => {
	  return 0;
	}
	
}
