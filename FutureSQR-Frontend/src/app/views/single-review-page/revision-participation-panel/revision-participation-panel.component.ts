import { Component, OnInit, Input, Output, SimpleChanges, EventEmitter } from '@angular/core';

// should be a uimodel instead of a backend model
import { BackendModelReviewData } from '../../../backend/model/backend-model-review-data';


@Component({
  selector: 'app-revision-participation-panel',
  templateUrl: './revision-participation-panel.component.html',
  styleUrls: ['./revision-participation-panel.component.css']
})
export class RevisionParticipationPanelComponent implements OnInit {
	
	public currentUiReviewData: BackendModelReviewData = new BackendModelReviewData();
	
	@Input() activeReviewData: BackendModelReviewData = new BackendModelReviewData();
	@Output() onRevisionStateChanged = new EventEmitter<string>();

	constructor() { }

	ngOnInit(): void {
	}
	
	ngOnChanges(changes: SimpleChanges): void {
		let reviewDataCandidate:BackendModelReviewData = changes.activeReviewData.currentValue;
		
		if(this.currentUiReviewData != reviewDataCandidate) {
			this.currentUiReviewData = reviewDataCandidate;
		}
	}
	

	onAddRevisions(): void {
		
	}
}
