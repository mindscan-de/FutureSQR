import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
import { Output, EventEmitter } from '@angular/core';

// Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';


// Backend-Models
import { BackendModelReviewData } from '../../../backend/model/backend-model-review-data';
import { BackendModelReviewResult } from '../../../backend/model/backend-model-review-result';

@Component({
  selector: 'app-open-review-item',
  templateUrl: './open-review-item.component.html',
  styleUrls: ['./open-review-item.component.css']
})
export class OpenReviewItemComponent implements OnInit {

	public currentReviewers: Map<string,BackendModelReviewResult> = new Map<string, BackendModelReviewResult>();
	
	@Input() activeProjectID: string;
	@Input() openReview: BackendModelReviewData;
	@Output() reviewUpdated: EventEmitter<string> = new EventEmitter<string>();
	
	
	constructor(private projectDataQueryBackend : ProjectDataQueryBackendService) { }

	ngOnInit(): void {
	}
	
	ngOnChanges(changes:SimpleChanges):void {
		
		if(changes.openReview != undefined) {
			this.currentReviewers = new Map<string, BackendModelReviewResult>(Object.entries(changes.openReview.currentValue.reviewReviewersResults));
		}
	}
	
	onCloseReview():void {
		if(this.openReview.reviewReadyToClose) {
			this.projectDataQueryBackend.closeReview(this.openReview.reviewFkProjectId, this.openReview.reviewId).subscribe(
				data => {
					this.reviewUpdated.emit("review closed");
				},
				error => {}				
			);
		} 
	}

}
