import { Component, OnInit } from '@angular/core';
import { KeyValue } from '@angular/common';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';


// Services
import { UserDataQueryBackendService } from '../../backend/services/user-data-query-backend.service';

// Backend Models
import { BackendModelReviewData } from '../../backend/model/backend-model-review-data';
import { BackendModelSimpleUserEntry } from '../../backend/model/backend-model-simple-user-entry';
import { BackendModelSimpleUserDictionary } from '../../backend/model/backend-model-simple-user-dictionary';

@Component({
  selector: 'app-add-participant-to-review-selection-dialog',
  templateUrl: './add-participant-to-review-selection-dialog.component.html',
  styleUrls: ['./add-participant-to-review-selection-dialog.component.css']
})
export class AddParticipantToReviewSelectionDialogComponent implements OnInit {

	public currentUiReviewData : BackendModelReviewData = new BackendModelReviewData();
	public currentUserMap : Map<String,BackendModelSimpleUserEntry> = new Map<String,BackendModelSimpleUserEntry>();

	constructor(
		private userDataQueryBackend: UserDataQueryBackendService,
		public activeModal: NgbActiveModal
	) { }

	ngOnInit(): void {
		// TODO query the user data query Backend for a user list
		this.userDataQueryBackend.getSimpleUserDictionary().subscribe(
			data => {this.onUserMapProvided(data)},
			error => {} 
		);
		
		// MAYBE do this in order,
		// TODO query a suggestion shortlist for the review (based on file, and other metrics?)
	}
	
	onUserMapProvided(userDictionary:BackendModelSimpleUserDictionary):void {
		this.currentUserMap = userDictionary.dictionary;
	}
	
	// add some on data provided information....
	
	// for full userdata list (maybe with some filters or so...)
	// for reviewer shortlist
	
	// Add some setters
	setActiveReviewData(activeReviewData: BackendModelReviewData): void {
		this.currentUiReviewData = activeReviewData;
	}
	

	// used for user map iteration. 	
	public originalOrder = (a: KeyValue<number,string>, b: KeyValue<number,string>): number => {
	  return 0;
	}

}
