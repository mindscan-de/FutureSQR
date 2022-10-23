import { Component, OnInit } from '@angular/core';

// Admin App Services
import { AdminNavigationBarService }  from '../../services/admin-navigation-bar.service';
import { AdminNavbarBreadcrumbItem } from '../../services/model/admin-navbar-breadcrumb-item';

@Component({
  selector: 'app-configure-projects',
  templateUrl: './configure-projects.component.html',
  styleUrls: ['./configure-projects.component.css']
})
export class ConfigureProjectsComponent implements OnInit {

	constructor(
 		private adminNavigationBarService : AdminNavigationBarService	
	) { }

	ngOnInit(): void {
		this.updateNavigationBar();
	}
	
	updateNavigationBar() : void {
		let x:AdminNavbarBreadcrumbItem[] = []
		
		x.push(new AdminNavbarBreadcrumbItem('projects', ['projects'], true));
		 
		this.adminNavigationBarService.setAdminBreadCrumbNavigation(x);
	}

}
