import { Component, OnInit, Input,  SimpleChanges } from '@angular/core';

import { BackendModelProjectRecentCommitRevision } from '../../../backend/model/backend-model-project-recent-commit-revision';


@Component({
  selector: 'app-revision-action-panel',
  templateUrl: './revision-action-panel.component.html',
  styleUrls: ['./revision-action-panel.component.css']
})
export class RevisionActionPanelComponent implements OnInit {
	
	@Input() activeProjectID: string = "";
	@Input() activeRevisionData: BackendModelProjectRecentCommitRevision = new BackendModelProjectRecentCommitRevision();	

	constructor() { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		
	}

}
