import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';

// Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';

// UI Model
import { UiReviewFileInformation } from '../../../commonui/uimodel/ui-review-file-information';

// Backend Model
import { BackendModelSingleCommitFileActionsInfo } from '../../../backend/model/backend-model-single-commit-file-actions-info';
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
	
	constructor( private projectDataQueryBackend : ProjectDataQueryBackendService, private router: Router  ) { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
	}
	
	onCreateReview(projectId: string, revisionId: string) : void {
		this.projectDataQueryBackend.createNewReview(projectId, revisionId).subscribe (
			data => {
				// TODO redirect o review page.
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
		
		let that = this;
		
		// otherwise retrieve the list and then show the list.
		
		this.projectDataQueryBackend.getRecentProjectRevisionFilePathsData(this.activeProjectID,this.activeRevision.revisionid).subscribe(
			data =>  this.onFileListActionsProvided(data),
			error => console.log(error)
		);
	}
	
	
	// TODO: retrieve file list for this version
	
	onFileListActionsProvided( fileChanges: BackendModelSingleCommitFileActionsInfo) : void {
		let fileInformations : UiReviewFileInformation[] = [];
		
		let map = fileChanges.fileActionMap;
		for(let i: number = 0;i<map.length;i++) {
			let fileInfo: UiReviewFileInformation = new UiReviewFileInformation( map[i][1], map[i][0], true );
			fileInformations.push(fileInfo);
		}
		this.uiFileInformations = fileInformations;
		
		this.showFileList = !this.showFileList;
	}
	
}


