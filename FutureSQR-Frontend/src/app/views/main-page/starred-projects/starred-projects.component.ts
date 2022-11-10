import { Component, OnInit } from '@angular/core';

// Backend Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';

// Internal Services
import { CurrentUserService } from '../../../uiservices/current-user.service';

// BackendModel - should be actually 
import { BackendModelProjectItem } from '../../../backend/model/backend-model-project-item';


@Component({
  selector: 'app-starred-projects',
  templateUrl: './starred-projects.component.html',
  styleUrls: ['./starred-projects.component.css']
})
export class StarredProjectsComponent implements OnInit {
	
	// contains the ui data for the starred projects
	// actually this should be a ui model. 
	public uiModelStarredProjects: BackendModelProjectItem[] = [];
  
	constructor( 
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private currentUserService : CurrentUserService	
	) { }

	ngOnInit(): void {
		let currentUserUUID: string = this.currentUserService.getCurrentUserUUID();
	
		this.projectDataQueryBackend.getStarredProjects(currentUserUUID).subscribe( 
			data => this.onStarredProjectsProvided(data),
			error => console.log(error)
		);
	}

	onStarredProjectsProvided( starredProjects: BackendModelProjectItem[]) : void {
		this.uiModelStarredProjects = starredProjects;
	}

}
