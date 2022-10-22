import { Component, OnInit, Input,  SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';

// Backend Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';

// Internal Services
import { CurrentUserService } from '../../../services/current-user.service';

// Backend Model
import { BackendModelProjectRecentCommitRevision } from '../../../backend/model/backend-model-project-recent-commit-revision';


@Component({
  selector: 'app-revision-action-panel',
  templateUrl: './revision-action-panel.component.html',
  styleUrls: ['./revision-action-panel.component.css']
})
export class RevisionActionPanelComponent implements OnInit {
	
	@Input() activeProjectID: string = "";
	@Input() activeRevisionData: BackendModelProjectRecentCommitRevision = new BackendModelProjectRecentCommitRevision();	

	constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private currentUserService : CurrentUserService,
		private router: Router 
	) { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		
	}

	onCreateReview(projectId: string, revisionId: string) : void {
		let opening_userid = this.currentUserService.getCurrentUserUUID();
		
		this.projectDataQueryBackend.createNewReview(projectId, revisionId, opening_userid).subscribe (
			data => {
				// redirect to review page.
				this.router.navigate(['/', projectId, 'review', data['reviewId']]);
			},
			error => {}
		);
	}

}
