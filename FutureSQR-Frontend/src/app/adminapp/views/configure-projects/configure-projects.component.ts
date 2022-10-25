import { Component, OnInit } from '@angular/core';

// Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';

// Admin App Services
import { AdminNavigationBarService }  from '../../services/admin-navigation-bar.service';
import { AdminNavbarBreadcrumbItem } from '../../services/model/admin-navbar-breadcrumb-item';

// BackendModel - should be actually 
import { BackendModelProjectItem } from '../../../backend/model/backend-model-project-item';


@Component({
  selector: 'app-configure-projects',
  templateUrl: './configure-projects.component.html',
  styleUrls: ['./configure-projects.component.css']
})
export class ConfigureProjectsComponent implements OnInit {
	
	public uiModelAllProjects: BackendModelProjectItem[] = [];

	constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,		
 		private adminNavigationBarService : AdminNavigationBarService
	) { }

	ngOnInit(): void {
		this.updateNavigationBar();
		
		this.projectDataQueryBackend.getAllProjects().subscribe( 
			data => this.onAllProjectsProvided(data),
			error => console.log(error)
		);
	}
	
	onAllProjectsProvided( allProjects: BackendModelProjectItem[]) : void {
		this.uiModelAllProjects = allProjects;
	}
	
	
	updateNavigationBar() : void {
		let x:AdminNavbarBreadcrumbItem[] = []
		
		x.push(new AdminNavbarBreadcrumbItem('projects', ['projects'], true));
		 
		this.adminNavigationBarService.setAdminBreadCrumbNavigation(x);
	}

}
