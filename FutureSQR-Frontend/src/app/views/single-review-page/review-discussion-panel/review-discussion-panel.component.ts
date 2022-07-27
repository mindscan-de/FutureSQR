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
		
	}

}
