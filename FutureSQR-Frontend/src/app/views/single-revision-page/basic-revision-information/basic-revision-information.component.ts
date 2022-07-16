import { Component, OnInit, Input,  SimpleChanges } from '@angular/core';

import { BackendModelProjectRecentCommitRevision } from '../../../backend/model/backend-model-project-recent-commit-revision';

@Component({
  selector: 'app-basic-revision-information',
  templateUrl: './basic-revision-information.component.html',
  styleUrls: ['./basic-revision-information.component.css']
})
export class BasicRevisionInformationComponent implements OnInit {
	
	@Input() activeProjectID: string = "";
	@Input() activeRevisionID: string = "";
	@Input() activeRevisionData: BackendModelProjectRecentCommitRevision = new BackendModelProjectRecentCommitRevision();

	constructor() { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		
	}

}
