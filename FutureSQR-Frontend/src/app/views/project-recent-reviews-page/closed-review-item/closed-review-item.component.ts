import { Component, OnInit, Input } from '@angular/core';

// Backend Model
import { BackendModelReviewData } from '../../../backend/model/backend-model-review-data';


@Component({
  selector: 'app-closed-review-item',
  templateUrl: './closed-review-item.component.html',
  styleUrls: ['./closed-review-item.component.css']
})
export class ClosedReviewItemComponent implements OnInit {
	
	@Input() activeProjectID: string;
	@Input() closedReview:BackendModelReviewData;

	constructor() { }

	ngOnInit(): void {
	}

}
