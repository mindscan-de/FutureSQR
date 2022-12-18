import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

// backend model - should be a ui model.
import { BackendModelProjectRecentCommitRevision } from '../../../backend/model/backend-model-project-recent-commit-revision';

@Component({
  selector: 'app-other-file-revisions-panel',
  templateUrl: './other-file-revisions-panel.component.html',
  styleUrls: ['./other-file-revisions-panel.component.css']
})
export class OtherFileRevisionsPanelComponent implements OnInit {
	// needed for the links
	public uiActiveProjectID: string = "";
	// needed for the highlighting
	public uiActiveRevisionID: string = "";
	// needed for the list of revisions
	public uiOtherRevisions: BackendModelProjectRecentCommitRevision[] = [];

	
	@Input() activeProjectID: string = "";
	private activeProjectIDProvided = false;
	@Input() activeRevisionID: string = "";
	private activeRevisionIDProvided = false;
	@Input() otherRevisions: BackendModelProjectRecentCommitRevision[] = [];
	private otherRevisionsProvided = false;


	constructor() { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		if(changes.activeProjectID != undefined) {
			this.uiActiveProjectID = changes.activeProjectID.currentValue; 
			this.activeProjectIDProvided = true;
		}
		
		if(changes.activeRevisionID != undefined) {
			this.uiActiveRevisionID = changes.activeRevisionID.currentValue;
			this.activeProjectIDProvided = true;
		}
		
		if(changes.otherRevisions != undefined) {
			this.uiOtherRevisions = changes.otherRevisions.currentValue;
			this.otherRevisionsProvided = true;
		} 
	}
}
