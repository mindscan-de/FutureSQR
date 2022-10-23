import { Component, OnInit } from '@angular/core';

// Admin App Services
import { AdminNavigationBarService }  from '../../services/admin-navigation-bar.service';
import { AdminNavbarBreadcrumbItem } from '../../services/model/admin-navbar-breadcrumb-item';


@Component({
  selector: 'app-configure-groups',
  templateUrl: './configure-groups.component.html',
  styleUrls: ['./configure-groups.component.css']
})
export class ConfigureGroupsComponent implements OnInit {

	constructor(
 		private adminNavigationBarService : AdminNavigationBarService
	) { }

	ngOnInit(): void {
		this.updateNavigationBar();
	}

	updateNavigationBar() : void {
		let x:AdminNavbarBreadcrumbItem[] = []
		
		x.push(new AdminNavbarBreadcrumbItem('groups', ['groups'], true));
		 
		this.adminNavigationBarService.setAdminBreadCrumbNavigation(x);
	}

}
