import { Component, OnInit, Input,  SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-review-discussion-panel',
  templateUrl: './review-discussion-panel.component.html',
  styleUrls: ['./review-discussion-panel.component.css']
})
export class ReviewDiscussionPanelComponent implements OnInit {
	
	@Input() activeProjectID: string = "";
	@Input() activeReviewID: string = "";
	

	constructor() { }
	
	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		// TODO: use backend service to retrieve discussion data, 
		// when both values are valid.
		
		// the result of this query needs to be transformed into a ui model,
		// especially transform the map data, because the map is not an object
		// but must be converted from an object into a Map.
	}

}
