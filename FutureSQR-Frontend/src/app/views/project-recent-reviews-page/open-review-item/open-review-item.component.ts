import { Component, OnInit, Input, SimpleChanges } from '@angular/core';


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
	
	
	// TODO: we want to present a list of reviewers and their current state, such you can see which are open for what reason.

	constructor() { }

	ngOnInit(): void {
	}
	
	ngOnChanges(changes:SimpleChanges):void {
		
		if(changes.openReview != undefined) {
			this.currentReviewers = new Map<string, BackendModelReviewResult>(Object.entries(changes.openReview.currentValue.reviewReviewersResults));
		}
	}

}
