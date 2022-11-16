import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { AdminDataQueryBackendService } from '../../backend/services/admin-data-query-backend.service';

// Admin App Services
import { AdminNavigationBarService }  from '../../services/admin-navigation-bar.service';
import { AdminNavbarBreadcrumbItem } from '../../services/model/admin-navbar-breadcrumb-item';


@Component({
  selector: 'app-configure-user',
  templateUrl: './configure-user.component.html',
  styleUrls: ['./configure-user.component.css']
})
export class ConfigureUserComponent implements OnInit {
	
	public activeUserID: string = '';

	constructor(
		private adminNavigationBarService : AdminNavigationBarService,
		private adminDataQueryBackend : AdminDataQueryBackendService,
		private route: ActivatedRoute, 
		private router: Router
	) { }

	ngOnInit(): void {
		this.activeUserID = this.route.snapshot.paramMap.get('useruuid');

		this.updateNavigationBar(this.activeUserID);
	}

	updateNavigationBar(username : string) : void {
		let x:AdminNavbarBreadcrumbItem[] = []
		
		x.push(new AdminNavbarBreadcrumbItem('users', ['users'], false));
		x.push(new AdminNavbarBreadcrumbItem(username, ['user',this.activeUserID], true));
		 
		this.adminNavigationBarService.setAdminBreadCrumbNavigation(x);
	}

}
