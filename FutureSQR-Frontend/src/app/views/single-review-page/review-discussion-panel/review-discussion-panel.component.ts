import { Component, OnInit, Input,  SimpleChanges } from '@angular/core';
import { FormControl } from '@angular/forms';

// Backend Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';

// Internal Services
import { CurrentUserService } from '../../../uiservices/current-user.service';


// Backend-Model
import { BackendModelThreadsData } from '../../../backend/model/backend-model-threads-data';


@Component({
  selector: 'app-review-discussion-panel',
  templateUrl: './review-discussion-panel.component.html',
  styleUrls: ['./review-discussion-panel.component.css']
})
export class ReviewDiscussionPanelComponent implements OnInit {
	
	public formDiscussionText = new FormControl();
	
	public uimodel: BackendModelThreadsData = new BackendModelThreadsData();
	
	@Input() activeProjectID: string = "";
	@Input() activeReviewID: string = "";
	
	constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private currentUserService : CurrentUserService
		) { }
	
	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		// TODO: use backend service to retrieve discussion data, 
		// when both values are valid.
		
		// the result of this query needs to be transformed into a ui model,
		// especially transform the map data, because the map is not an object
		// but must be converted from an object into a Map.
		
		// XXX: Quick hack to get it done
		this.retrieveDiscussion();
	}
	
	retrieveDiscussion() : void {
		if((this.activeProjectID == undefined) || (this.activeProjectID=="")) {
			return;
		} 
		
		if((this.activeReviewID == undefined) || (this.activeReviewID=="")) {
			return;
		} 
		
		this.projectDataQueryBackend
			.getThreadData(this.activeProjectID, this.activeReviewID).subscribe(
				data => { this.onThreadsDataReceived(data)},
				error => {}
			);
	}
	
	onThreadsDataReceived(data:BackendModelThreadsData) : void {
		this.uimodel = data;
	}
	
	
	onCreateThreadClicked(): void {
		let that = this;
		let disussionText = this.formDiscussionText.value;
		let currentUserId = this.currentUserService.getCurrentUserUUID();
		
		// make sure the string is not empy.
		if(!this.isBlank(disussionText)) {
			this.projectDataQueryBackend
				.createThreadForReview(
					this.activeProjectID, 
					this.activeReviewID, 
					currentUserId, 
					disussionText).subscribe(
						data => { 
							that.retrieveDiscussion();
							that.formDiscussionText.setValue("");
						 },
						error => {}
				);
		}
	}
	
	isBlank(str) {
    	return (!str || /^\s*$/.test(str));
	}
	
	onThreadUpdated(event:string): void {
		// TODO: the idea would be in future to only read the updated thread
		this.retrieveDiscussion();
	}

}
