import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

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
		private route: ActivatedRoute, 
		private router: Router
	) { }

	ngOnInit(): void {
		this.activeUserID = this.route.snapshot.paramMap.get('useruuid');
		
		this.updateNavigationBar();
	}

	updateNavigationBar() : void {
		let x:AdminNavbarBreadcrumbItem[] = []
		
		x.push(new AdminNavbarBreadcrumbItem('users', ['users'], false));
		x.push(new AdminNavbarBreadcrumbItem(this.activeUserID, ['user',this.activeUserID], true));
		 
		this.adminNavigationBarService.setAdminBreadCrumbNavigation(x);
	}

}
