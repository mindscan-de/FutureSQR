import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Admin App Services
import { AdminNavigationBarService }  from '../../services/admin-navigation-bar.service';
import { AdminNavbarBreadcrumbItem } from '../../services/model/admin-navbar-breadcrumb-item';



@Component({
  selector: 'app-configure-group',
  templateUrl: './configure-group.component.html',
  styleUrls: ['./configure-group.component.css']
})
export class ConfigureGroupComponent implements OnInit {

	public activeGroupID: string = '';

	constructor(
		private adminNavigationBarService : AdminNavigationBarService,
		private route: ActivatedRoute, 
		private router: Router
	) { }

	ngOnInit(): void {
		this.activeGroupID = this.route.snapshot.paramMap.get('groupuuid');
		
		this.updateNavigationBar();
	}

	updateNavigationBar() : void {
		let x:AdminNavbarBreadcrumbItem[] = []
		
		x.push(new AdminNavbarBreadcrumbItem('groups', ['groups'], false));
		x.push(new AdminNavbarBreadcrumbItem(this.activeGroupID, ['group',this.activeGroupID], true));
		 
		this.adminNavigationBarService.setAdminBreadCrumbNavigation(x);
	}


}
