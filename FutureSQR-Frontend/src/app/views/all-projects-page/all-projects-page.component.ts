import { Component, OnInit } from '@angular/core';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// BackendModel - should be actually 
import { BackendModelProjectItem } from '../../backend/model/backend-model-project-item';


@Component({
  selector: 'app-all-projects-page',
  templateUrl: './all-projects-page.component.html',
  styleUrls: ['./all-projects-page.component.css']
})
export class AllProjectsPageComponent implements OnInit {

  public uiModelAllProjects: BackendModelProjectItem[] = [];

  constructor( private projectDataQueryBackend : ProjectDataQueryBackendService ) { }

  ngOnInit(): void {
	this.projectDataQueryBackend.getAllProjects().subscribe( 
		data => this.onAllProjectsProvided(data),
		error => console.log(error)
	);
  }

	onAllProjectsProvided( allProjects: BackendModelProjectItem[]) : void {
		this.uiModelAllProjects = allProjects;
	}

	onStarMe(activeProjectId:string): void {
		this.projectDataQueryBackend.starProject(activeProjectId).subscribe(
			data=>{
				// TODO react -> update local listitem for projectid and current new state.
			},
			error=>{}
		);
	}
	
	onUnstarMe(activeProjectId:string): void {
		this.projectDataQueryBackend.unstarProject(activeProjectId).subscribe(
			data =>{
				// TODO react -> update local listitem for projectid and current new state. 
			},
			error=>{},
		);
	}


}
