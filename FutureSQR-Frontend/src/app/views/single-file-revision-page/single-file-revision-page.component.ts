import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// backend services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// internal services
import { NavigationBarService } from '../../uiservices/navigation-bar.service';

// BackendModel - should be actually a ui model
import { BackendModelProjectRecentCommitRevision } from '../../backend/model/backend-model-project-recent-commit-revision';

@Component({
  selector: 'app-single-file-revision-page',
  templateUrl: './single-file-revision-page.component.html',
  styleUrls: ['./single-file-revision-page.component.css']
})
export class SingleFileRevisionPageComponent implements OnInit {
	
	public activeProjectID: string = '';
	public activeRevisionID: string = '';
	public activeFilePath: string = '';
	
	// TODO: make this a ui model in future.
	public uiRevisionData: BackendModelProjectRecentCommitRevision = new BackendModelProjectRecentCommitRevision();

	constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private navigationBarService : NavigationBarService,
		private route: ActivatedRoute, 
	) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		this.activeRevisionID = this.route.snapshot.paramMap.get('revisionid');
		
		this.activeFilePath = this.route.snapshot.queryParamMap.get('p');
		
		// should i request the "getRecentProjectRevisionFilePathsData" - so that the other file contents can be retrieved...
		
		this.projectDataQueryBackend.getRecentProjectRevisionInformation(this.activeProjectID,this.activeRevisionID).subscribe(
			data => this.onRevisionInformationProvided(data),
			error => console.log(error)
		);
	}
	
	onRevisionInformationProvided( revisionData: BackendModelProjectRecentCommitRevision): void {
		this.uiRevisionData = revisionData;
		
		this.setNavigation();
	}
	
	setNavigation(): void {
		// add navigation
		let x = []
		x.push(this.navigationBarService.createItem( this.activeProjectID, ['/',this.activeProjectID], false ));
		x.push(this.navigationBarService.createItem( this.uiRevisionData.shortrev, ['/',this.activeProjectID, 'revision', this.activeRevisionID], false ));
		
		// ['/',this.activeProjectID, 'revision', this.activeRevisionID, 'viewfile'] - problem with this approach is the url query parameter.
		x.push(this.navigationBarService.createItem( 'todo:filename',['/',this.activeProjectID, 'revision', this.activeRevisionID, 'viewfile'], true));
		
		this.navigationBarService.setBreadcrumbNavigation(x);
	}

}
