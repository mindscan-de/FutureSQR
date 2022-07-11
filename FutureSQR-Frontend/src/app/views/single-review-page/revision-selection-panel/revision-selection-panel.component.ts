import { Component, OnInit, Input,  SimpleChanges } from '@angular/core';

import { BackendModelReviewData } from '../../../backend/model/backend-model-review-data';
import { BackendModelProjectRecentCommitRevision } from '../../../backend/model/backend-model-project-recent-commit-revision';

@Component({
  selector: 'app-revision-selection-panel',
  templateUrl: './revision-selection-panel.component.html',
  styleUrls: ['./revision-selection-panel.component.css']
})
export class RevisionSelectionPanelComponent implements OnInit {

	public currentUiReviewRevisions: BackendModelProjectRecentCommitRevision [] = [];

	@Input() activeReviewData: BackendModelReviewData = new BackendModelReviewData();
	@Input() reviewRevisions: BackendModelProjectRecentCommitRevision[] = [];	

	constructor() { }

	ngOnInit(): void {
	}
	
	ngOnChanges(changes: SimpleChanges): void {
		
		if(changes.reviewRevisions !== undefined) {
			this.currentUiReviewRevisions = changes.reviewRevisions.currentValue.reverse(); 
		}
	}
	
	// todo: convert to ui model

	m2mTransform(inputList: BackendModelProjectRecentCommitRevision[]) : void {
		
	}
}
