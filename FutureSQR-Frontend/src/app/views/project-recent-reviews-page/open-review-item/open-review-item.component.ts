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

	constructor() { }
	
	
	

	ngOnInit(): void {
	}

}
