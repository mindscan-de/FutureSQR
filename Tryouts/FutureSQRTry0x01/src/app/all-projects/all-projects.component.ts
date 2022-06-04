import { Component, OnInit } from '@angular/core';

// Services
import { ProjectDataQueryBackendService } from '../backend-service/project-data-query-backend.service';

// UI-Model (TODO: currently we use the backend model directly, but the backend model should be translated to a ui model) 

// BackendModel
import { BackendModelProjectItem } from '../backend-service/project-model/backend-model-project-item';


@Component({
  selector: 'app-all-projects',
  templateUrl: './all-projects.component.html',
  styleUrls: ['./all-projects.component.scss']
})
export class AllProjectsComponent implements OnInit {
	
	public uiModelAllProjects: BackendModelProjectItem[] = [];

  constructor(private projectDataQueryBackend : ProjectDataQueryBackendService ) { }

  ngOnInit(): void {
	this.projectDataQueryBackend.getAllProjects().subscribe( 
		data => this.onAllProjectsProvided(data),
		error => console.log(error)
	);
  }

  onAllProjectsProvided( allProjects: BackendModelProjectItem[]) : void {
	this.uiModelAllProjects = allProjects;
  }

}
