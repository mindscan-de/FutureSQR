import { Component, OnInit, Input,  SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';

// Backend Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';

// Internal Services
import { CurrentUserService } from '../../../uiservices/current-user.service';

// Backend Model
import { BackendModelProjectRecentCommitRevision } from '../../../backend/model/backend-model-project-recent-commit-revision';
import { BackendModelProjectRecentReviews } from '../../../backend/model/backend-model-project-recent-reviews';


@Component({
  selector: 'app-revision-action-panel',
  templateUrl: './revision-action-panel.component.html',
  styleUrls: ['./revision-action-panel.component.css']
})
export class RevisionActionPanelComponent implements OnInit {
	
	@Input() activeProjectID: string = "";
	@Input() activeRevisionData: BackendModelProjectRecentCommitRevision = new BackendModelProjectRecentCommitRevision();
	
	public recentReviews:BackendModelProjectRecentReviews = new BackendModelProjectRecentReviews();
	public isAttachDisabled: boolean = true;

	constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private currentUserService : CurrentUserService,
		private router: Router 
	) { }

	ngOnInit(): void {
		 
	}

	ngOnChanges(changes: SimpleChanges): void {
		if(changes.activeProjectID != undefined) {
			let projectid:string = changes.activeProjectID.currentValue;

			this.projectDataQueryBackend.getRecentReviewsByProject(projectid).subscribe(
				data => {
					this.recentReviews = data;
					this.isAttachDisabled = data.openReviews.length == 0;
				 },
				error => { }
			);
		}
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
	
	onAttachToReview(projectId: string, revisionId: string) : void {
		let userid = this.currentUserService.getCurrentUserUUID();
		
		// TODO : open a modal dialog, to and provide all current open reviews
		// let the user select a review, return with this result.
		
		// reuse the add revision to review selection dialog, but configure differently?
		
		// Actually ths "attachToReveiw" button should be disabled, if no open reviews exist.
		
		// then use append revision to review, to add this selected (open) review. 
/*		this.projectDataQueryBackend.appendRevisionToReview(projectid, reviewid, revisionid, userid).subscribe (
			data => {
				// redirect to review page.
				this.router.navigate(['/', projectId, 'review', data['reviewId']]);
			},
			error => {}
		);
*/		
		 
	}

}
