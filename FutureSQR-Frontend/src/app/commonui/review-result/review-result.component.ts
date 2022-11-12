import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

import { BackendModelReviewResult } from '../../backend/model/backend-model-review-result';

@Component({
  selector: 'app-review-result',
  templateUrl: './review-result.component.html',
  styleUrls: ['./review-result.component.css']
})
export class ReviewResultComponent implements OnInit {

	@Input() reviewState:BackendModelReviewResult = new BackendModelReviewResult();

	constructor() { }

	ngOnInit(): void {
	}

}
