import { Component, OnInit } from '@angular/core';

// Services
import { ProjectDataQueryBackendService } from '../../backend-service/project-data-query-backend.service';

// UI-Model (TODO: currently we use the backend model directly, but the backend model should be translated to a ui model) 

// BackendModel
import { BackendModelProjectItem } from '../../backend-service/project-model/backend-model-project-item';


@Component({
  selector: 'app-starred-projects',
  templateUrl: './starred-projects.component.html',
  styleUrls: ['./starred-projects.component.scss']
})
export class StarredProjectsComponent implements OnInit {
	
	public uiModelStarredProjects: BackendModelProjectItem[] = [];

  constructor(private projectDataQueryBackend : ProjectDataQueryBackendService ) { }

  ngOnInit(): void {
	this.projectDataQueryBackend.getMyStarredProjects().subscribe( 
		data => this.onStarredProjectsProvided(data),
		error => console.log(error)
	);
  }

  onStarredProjectsProvided( starredProjects: BackendModelProjectItem[]) : void {
	this.uiModelStarredProjects = starredProjects;
  }


}
