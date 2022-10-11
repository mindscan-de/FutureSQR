import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';


// Services
import { UserDataQueryBackendService } from '../../backend/services/user-data-query-backend.service';

// Backend Models
import { BackendModelReviewData } from '../../backend/model/backend-model-review-data';

@Component({
  selector: 'app-add-participant-to-review-selection-dialog',
  templateUrl: './add-participant-to-review-selection-dialog.component.html',
  styleUrls: ['./add-participant-to-review-selection-dialog.component.css']
})
export class AddParticipantToReviewSelectionDialogComponent implements OnInit {

	public currentUiReviewData : BackendModelReviewData = new BackendModelReviewData();

	constructor(
		private userDataQueryBackend: UserDataQueryBackendService,
		public activeModal: NgbActiveModal
	) { }

	ngOnInit(): void {
		// TODO query the user data query Backend for a user list
		this.userDataQueryBackend.getSimpleUserDictionary();
		
/*		.subscribe(
			data => {this.onUserMapProvided(data)},
			error => {} 
		);
*/		// TODO query a suggestion shortlist for the review (based on file, and other metrics?)
	}
	
	onUserMapProvided(userMap:any):void {
		
	}
	
	// add some on data provided information....
	
	// for full userdata list (maybe with some filters or so...)
	// for reviewer shortlist
	
	// Add some setters
	setActiveReviewData(activeReviewData: BackendModelReviewData): void {
		this.currentUiReviewData = activeReviewData;
	}
	
	

}
