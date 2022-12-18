import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// backend services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// internal services
import { NavigationBarService } from '../../uiservices/navigation-bar.service';

// BackendModel - should be actually a ui model
import { BackendModelProjectRecentCommitRevision } from '../../backend/model/backend-model-project-recent-commit-revision';
import { BackendModelRevisionFileContent } from '../../backend/model/backend-model-revision-file-content';

// ui Model
import { UiReviewFileInformation } from '../../commonui/uimodel/ui-review-file-information';

@Component({
  selector: 'app-single-file-revision-page',
  templateUrl: './single-file-revision-page.component.html',
  styleUrls: ['./single-file-revision-page.component.css']
})
export class SingleFileRevisionPageComponent implements OnInit {
	
	public activeProjectID: string = '';
	public activeRevisionID: string = '';
	public activeFilePath: string = '';
	public uiActiveFileInformation: UiReviewFileInformation = new UiReviewFileInformation(".html","",true);
	public uiOtherFileRevisions: BackendModelProjectRecentCommitRevision[] = [];
	
	// TODO: make this a ui model in future.
	public uiRevisionData: BackendModelProjectRecentCommitRevision = new BackendModelProjectRecentCommitRevision();
	public uiRevisionFileContent: BackendModelRevisionFileContent = new BackendModelRevisionFileContent(); 
	

	constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private navigationBarService : NavigationBarService,
		private route: ActivatedRoute, 
	) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		this.activeRevisionID = this.route.snapshot.paramMap.get('revisionid');
		this.activeFilePath = this.route.snapshot.queryParamMap.get('p');
		this.uiActiveFileInformation = new UiReviewFileInformation(this.activeFilePath, "", true);
		
		// should i request the "getRecentProjectRevisionFilePathsData" - so that the other file contents can be retrieved...
		
		this.projectDataQueryBackend.getRecentProjectRevisionInformation(this.activeProjectID,this.activeRevisionID).subscribe(
			data => this.onRevisionInformationProvided(data),
			error => console.log(error)
		);
		
		this.projectDataQueryBackend.getParticularFileRevisionContent(this.activeProjectID, this.activeRevisionID, this.activeFilePath).subscribe(
			data => this.onFileContentForRevisionProvided(data),
			error => console.log(error)
		);
	}
	
	onRevisionInformationProvided( revisionData: BackendModelProjectRecentCommitRevision): void {
		this.uiRevisionData = revisionData;
		
		this.setNavigation();
	}
	
	onFileContentForRevisionProvided( revisionFileContent: BackendModelRevisionFileContent): void {
		this.uiRevisionFileContent = revisionFileContent;
		
	}
	
	setNavigation(): void {
		// add navigation
		let x = []
		x.push(this.navigationBarService.createItem( this.activeProjectID, ['/',this.activeProjectID], false ));
		x.push(this.navigationBarService.createItem( this.uiRevisionData.shortrev, ['/',this.activeProjectID, 'revision', this.activeRevisionID], false ));
		x.push(this.navigationBarService.createItemWithQueryParameters( this.activeFilePath,['/',this.activeProjectID, 'revision', this.activeRevisionID, 'viewfile'], {'p':this.activeFilePath}, true));
		
		this.navigationBarService.setBreadcrumbNavigation(x);
	}

}
