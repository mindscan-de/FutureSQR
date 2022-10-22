import { Component, OnInit, ChangeDetectorRef } from '@angular/core';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';
import { UserDataQueryBackendService } from '../../backend/services/user-data-query-backend.service';

import { NavigationBarService } from '../../services/navigation-bar.service';
import { NavbarBreadcrumbItem } from '../../services/model/navbar-breadcrumb-item';
import { CurrentUserService } from '../../services/current-user.service';

// BackendModel - should be actually 
import { BackendModelProjectItem } from '../../backend/model/backend-model-project-item';


@Component({
  selector: 'app-all-projects-page',
  templateUrl: './all-projects-page.component.html',
  styleUrls: ['./all-projects-page.component.css']
})
export class AllProjectsPageComponent implements OnInit {

  public uiModelAllProjects: BackendModelProjectItem[] = [];

	constructor( 
		private projectDataQueryBackend : ProjectDataQueryBackendService, 
		private navbarService: NavigationBarService,
		private currentUserService: CurrentUserServce,
		private cdr: ChangeDetectorRef
	) { }

	ngOnInit(): void {
		this.projectDataQueryBackend.getAllProjects().subscribe( 
			data => this.onAllProjectsProvided(data),
			error => console.log(error)
		);
	
		// add navigation
		let x = []
		x.push(new NavbarBreadcrumbItem( 'all projects', ['/','allprojects'], true ));
		this.navbarService.setBreadcrumbNavigation(x);		
  }

	onAllProjectsProvided( allProjects: BackendModelProjectItem[]) : void {
		this.uiModelAllProjects = allProjects;
	}

	onStarMe(activeProjectId:string): void {
		let currentUserUUID:string = this.currentUserService.getCurrentUserUUID();
		
		this.projectDataQueryBackend.starProject(activeProjectId, currentUserUUID).subscribe(
			data=>{
				// TODO react -> update local listitem for projectid and current new state.
				this.ui_update_star(activeProjectId, true);
			},
			error=>{}
		);
		
	}
	
	onUnstarMe(activeProjectId:string): void {
		let currentUserUUID:string = this.currentUserService.getCurrentUserUUID();
		
		this.projectDataQueryBackend.unstarProject(activeProjectId, currentUserUUID).subscribe(
			data =>{
				// TODO react -> update local listitem for projectid and current new state.
				this.ui_update_star(activeProjectId, false); 
			},
			error=>{},
		);
	}

	ui_update_star(activeProjectId:string, newvalue:boolean): void {
		for (var i: number = 0; i<this.uiModelAllProjects.length;i++) {
			
			if(this.uiModelAllProjects[i]['project_id']==activeProjectId ) {
				this.uiModelAllProjects[i]['is_starred']=newvalue;
				this.cdr.detectChanges();
				break;
			}
		}
	}

}
