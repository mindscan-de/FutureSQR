import { Component, OnInit } from '@angular/core';

// Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';

// BackendModel
import { BackendModelProjectItem } from '../../../backend/model/backend-model-project-item';


@Component({
  selector: 'app-starred-projects',
  templateUrl: './starred-projects.component.html',
  styleUrls: ['./starred-projects.component.css']
})
export class StarredProjectsComponent implements OnInit {
	
  public uiModelStarredProjects: BackendModelProjectItem[] = [];

  
  constructor( private projectDataQueryBackend : ProjectDataQueryBackendService ) { }

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
