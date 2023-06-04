import { Component, OnInit } from '@angular/core';

// Admin App Services
import { AdminNavigationBarService }  from '../../services/admin-navigation-bar.service';
import { AdminNavbarBreadcrumbItem } from '../../services/model/admin-navbar-breadcrumb-item';

// Backend Service
import { AdminDataQueryBackendService } from '../../backend/services/admin-data-query-backend.service';


@Component({
  selector: 'app-configure-system-database',
  templateUrl: './configure-system-database.component.html',
  styleUrls: ['./configure-system-database.component.css']
})
export class ConfigureSystemDatabaseComponent implements OnInit {

	constructor(
		private adminDataQueryBackend : AdminDataQueryBackendService,		
 		private adminNavigationBarService : AdminNavigationBarService
		
	) { }

	ngOnInit(): void {
		this.updateNavigationBar();
	}
	
	updateNavigationBar() : void {
		let x:AdminNavbarBreadcrumbItem[] = []
		
		x.push(new AdminNavbarBreadcrumbItem('projects', ['projects'], false));
		x.push(new AdminNavbarBreadcrumbItem('database', ['system','database'], true));
		 
		this.adminNavigationBarService.setAdminBreadCrumbNavigation(x);
	}
	

	reinitializeDatabase(): void {
		console.log("Hello World");
		this.adminDataQueryBackend.postReinitDatabase().subscribe(
			data => {},
			error => {}
		);
	}
}
