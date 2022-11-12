import { Component, OnInit, Input, SimpleChanges } from '@angular/core';


import { UserLookupService }  from '../../uiservices/user-lookup.service';
import { UiUser } from '../../uiservices/model/ui-user';

import { BackendModelReviewResult } from '../../backend/model/backend-model-review-result';


@Component({
  selector: 'app-review-result',
  templateUrl: './review-result.component.html',
  styleUrls: ['./review-result.component.css']
})
export class ReviewResultComponent implements OnInit {

	public userInfo: UiUser;
	@Input() reviewState:BackendModelReviewResult = new BackendModelReviewResult();

	constructor(
		private userLookupService : UserLookupService
	) { 
		this.userInfo = this.userLookupService.unknown();
	}

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		if(changes.reviewState != undefined) {
			let reviewResult:BackendModelReviewResult = changes.reviewState.currentValue;
			
			this.userInfo = this.userLookupService.lookup(reviewResult.reviewer_id);
		}
	}

}
