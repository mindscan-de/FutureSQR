import { Component, OnInit, Input,  SimpleChanges } from '@angular/core';

// Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';


// Backend-Model
import { BackendModelThreadsData } from '../../../backend/model/backend-model-threads-data';


@Component({
  selector: 'app-review-discussion-panel',
  templateUrl: './review-discussion-panel.component.html',
  styleUrls: ['./review-discussion-panel.component.css']
})
export class ReviewDiscussionPanelComponent implements OnInit {
	
	@Input() activeProjectID: string = "";
	@Input() activeReviewID: string = "";
	

	constructor(private projectDataQueryBackend : ProjectDataQueryBackendService) { }
	
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
		
	}

}
