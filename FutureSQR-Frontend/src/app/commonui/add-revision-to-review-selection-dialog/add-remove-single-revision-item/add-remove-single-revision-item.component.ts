import { Component, Input, OnInit, SimpleChanges } from '@angular/core';


import { BackendModelProjectRecentCommitRevision } from '../../../backend/model/backend-model-project-recent-commit-revision';

@Component({
  selector: 'app-add-remove-single-revision-item',
  templateUrl: './add-remove-single-revision-item.component.html',
  styleUrls: ['./add-remove-single-revision-item.component.css']
})
export class AddRemoveSingleRevisionItemComponent implements OnInit {
	
	@Input() revision: BackendModelProjectRecentCommitRevision = new BackendModelProjectRecentCommitRevision();
	@Input() btnText: string = "btnText";
	@Input() title: string = "";

	constructor() { }

	ngOnInit(): void {
	}
	
	ngOnChanges(changes: SimpleChanges): void {
		if(changes.btnText!=undefined) {
			this.btnText = changes.btnText.currentValue;
		}
	}

	onButtonClicked( revisionId: string ): void  {
		// currentUiReviewData.reviewFkProjectId, currentUiReviewData.reviewId, revision.revisionid)
		
		// send the revison id to the hosting component.
		
		// TODO: emit event.
		// this should call the outer method.
		console.log("somthieng...")
	}
}
