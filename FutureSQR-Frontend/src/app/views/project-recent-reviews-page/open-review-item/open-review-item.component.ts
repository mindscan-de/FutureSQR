import { Component, OnInit, Input } from '@angular/core';


import { BackendModelReviewData } from '../../../backend/model/backend-model-review-data';

@Component({
  selector: 'app-open-review-item',
  templateUrl: './open-review-item.component.html',
  styleUrls: ['./open-review-item.component.css']
})
export class OpenReviewItemComponent implements OnInit {

	@Input() activeProjectID: string;
	@Input() openReview: BackendModelReviewData;
	
	// TODO: we want to present a close button if all participants are happy, so you can close reviews from here.
	// TODO: we want to present a list of reviewers and their current state, such you can see which are open for what reason.
	

	constructor() { }
	
	
	

	ngOnInit(): void {
	}

}
