import { Component, OnInit } from '@angular/core';

// Admin App Services
import { AdminNavigationBarService }  from '../../services/admin-navigation-bar.service';
import { AdminNavbarBreadcrumbItem } from '../../services/model/admin-navbar-breadcrumb-item';


@Component({
  selector: 'app-configure-project',
  templateUrl: './configure-project.component.html',
  styleUrls: ['./configure-project.component.css']
})
export class ConfigureProjectComponent implements OnInit {

	constructor(
		private adminNavigationBarService : AdminNavigationBarService
	) { }

	ngOnInit(): void {
		this.updateNavigationBar();
	}

	updateNavigationBar() : void {
		let x:AdminNavbarBreadcrumbItem[] = []
		
		x.push(new AdminNavbarBreadcrumbItem('projects', ['projects'], true));
		x.push(new AdminNavbarBreadcrumbItem('PROJECTNAME', ['project','PROJECTNAME'], true));
		 
		this.adminNavigationBarService.setAdminBreadCrumbNavigation(x);
	}

}
