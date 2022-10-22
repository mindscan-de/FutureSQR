import { Component, OnInit } from '@angular/core';
import { KeyValue } from '@angular/common';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';


// Backend Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';
import { UserDataQueryBackendService } from '../../backend/services/user-data-query-backend.service';

// Internal Services
import { CurrentUserService } from '../../services/current-user.service';


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
	
	private currentUserMapActive : boolean = false;
	public currentUserMap : Map<string,BackendModelSimpleUserEntry> = new Map<string,BackendModelSimpleUserEntry>();
	
	private userUUIDsInShortList : string[] = [''];
	
	public usersInShortList : BackendModelSimpleUserEntry[] = []
	public usersInFilteredList : BackendModelSimpleUserEntry[] = []
	public suggestedUserMap : Map<string,BackendModelSimpleUserEntry> = new Map<string,BackendModelSimpleUserEntry>();

	constructor(
		private projectDataQueryBackend: ProjectDataQueryBackendService,
		private userDataQueryBackend: UserDataQueryBackendService,
		private currentUserService: CurrentUserService,
		public activeModal: NgbActiveModal
	) { }

	ngOnInit(): void {
		this.currentUserMapActive = false;
		// TODO query the user data query Backend for a user list
		this.userDataQueryBackend.getSimpleUserDictionary().subscribe(
			data => {this.onUserMapProvided(data)},
			error => {} 
		);
		
		this.projectDataQueryBackend.getSuggestedReviewers(
			this.currentUiReviewData.reviewFkProjectId,
			this.currentUiReviewData.reviewId
		).subscribe (
			data => {this.onSuggestedReviewersProvided(data)},
			error => {}
		);
		// MAYBE do this in order,
		// TODO query a suggestion shortlist for the review (based on file, and other metrics?)
		// MAYBE the shortlist should contain a reason?
		
		
	}
	
	onUserMapProvided(userDictionary:BackendModelSimpleUserDictionary):void {
		this.currentUserMap = userDictionary.dictionary;
		this.currentUserMapActive = true;
		
		this.updateShortList();
		this.updateFilteredList();
	}
	
	onSuggestedReviewersProvided(suggestedUsersDictionary:BackendModelSimpleUserDictionary ):void {
		this.suggestedUserMap = suggestedUsersDictionary.dictionary;
	}
	
	private updateShortList() : void {
		if(this.currentUserMapActive) {
			// use the currentUserMap to translate the uuid shortlist to 
			// a displayable shortlist
			// this.usersInShortList.map( (uuid) => { this.currentUserMap.get(uuid)})
		}
	}
	
	private updateFilteredList() : void {
		if(this.currentUserMapActive) {
			// TODO if there is empty filter limit it to first 10 elements
			// map values filter
		}
	}
	
	
	// add some on data provided information....
	
	// for full userdata list (maybe with some filters or so...)
	// for reviewer shortlist
	
	addParticipant(participant_uuid: string) : void {
		let current_user_uuid = this.currentUserService.getCurrentUserUUID();
		
		this.projectDataQueryBackend.addReviewer(
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
	}
	

	// used for user map iteration. 	
	public originalOrder = (a: KeyValue<number,string>, b: KeyValue<number,string>): number => {
	  return 0;
	}

}
