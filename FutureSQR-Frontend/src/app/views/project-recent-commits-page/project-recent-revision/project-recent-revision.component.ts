import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';

// Backend Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';

// Internal Services
import { CurrentUserService } from '../../../uiservices/current-user.service';

// M2M Transformation
import { TransformCommitRevision } from '../../../m2m/transform-commit-revision';

// UI Model
import { UiReviewFileInformation } from '../../../commonui/uimodel/ui-review-file-information';

// Backend Model
import { BackendModelProjectRecentCommitRevision } from '../../../backend/model/backend-model-project-recent-commit-revision';

@Component({
  selector: 'app-project-recent-revision',
  templateUrl: './project-recent-revision.component.html',
  styleUrls: ['./project-recent-revision.component.css']
})
export class ProjectRecentRevisionComponent implements OnInit {

	@Input() activeProjectID : string;
	@Input() activeRevision : BackendModelProjectRecentCommitRevision = undefined;
	
	public uiFileInformations: UiReviewFileInformation[] = undefined;
	
	public showFileList: boolean = false;
	
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
		let opening_userid:string = this.currentUserService.getCurrentUserUUID();
		this.projectDataQueryBackend.createNewReview(projectId, revisionId, opening_userid).subscribe (
			data => {
				// TODO redirect to review page.
				this.router.navigate(['/', projectId, 'review', data['reviewId']]);
			},
			error => {}
		);
	}
	
	// Open Close filelist
	onToggleFileList(): void {
		// if file list is known/received, then just toggle to show
		if(this.uiFileInformations != undefined) {
			this.showFileList = !this.showFileList;
			return;
		}
		
		// otherwise retrieve the list and then show the list.
		this.projectDataQueryBackend.getRecentProjectRevisionFilePathsData(this.activeProjectID,this.activeRevision.revisionid).subscribe(
			data =>  this.onFileListActionsProvided(TransformCommitRevision.convertToUiReviewFileinformationArray(data)),
			error => console.log(error)
		);
	}
	
	onFileListActionsProvided( fileInformations : UiReviewFileInformation[]) : void {
		this.uiFileInformations = fileInformations;
		this.showFileList = !this.showFileList;
	}
	
}


