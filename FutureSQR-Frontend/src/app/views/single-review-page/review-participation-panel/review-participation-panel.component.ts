import { Component, OnInit, Input, SimpleChanges  } from '@angular/core';


// Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';


// should be a uimodel instead of a backend model
import { BackendModelReviewData } from '../../../backend/model/backend-model-review-data';

@Component({
  selector: 'app-review-participation-panel',
  templateUrl: './review-participation-panel.component.html',
  styleUrls: ['./review-participation-panel.component.css']
})
export class ReviewParticipationPanelComponent implements OnInit {
	
	public currentUiReviewData: BackendModelReviewData = new BackendModelReviewData();
	
	@Input() activeReviewData: BackendModelReviewData = new BackendModelReviewData();

	constructor( private projectDataQueryBackend : ProjectDataQueryBackendService, ) { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		let reviewDataCandidate:BackendModelReviewData = changes.activeReviewData.currentValue;
		
		if(this.currentUiReviewData != reviewDataCandidate) {
			this.currentUiReviewData = reviewDataCandidate;
		}
	}
	
	onCloseReview(projectid:string, reviewId:string):void {
		// use the backend service to close this review.
		this.projectDataQueryBackend.closeReview(projectid, reviewId).subscribe(
			data => {
				// parent component should reload the page...
			},
			error => {}			
		);
	}
}
