import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Admin App Services
import { AdminNavigationBarService }  from '../../services/admin-navigation-bar.service';
import { AdminNavbarBreadcrumbItem } from '../../services/model/admin-navbar-breadcrumb-item';


@Component({
  selector: 'app-configure-project',
  templateUrl: './configure-project.component.html',
  styleUrls: ['./configure-project.component.css']
})
export class ConfigureProjectComponent implements OnInit {
	
	public activeProjectID: string = '';

	constructor(
		private adminNavigationBarService : AdminNavigationBarService,
		private route: ActivatedRoute, 
		private router: Router
	) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		
		this.updateNavigationBar();
	}

	updateNavigationBar() : void {
		let x:AdminNavbarBreadcrumbItem[] = []
		
		x.push(new AdminNavbarBreadcrumbItem('projects', ['projects'], false));
		x.push(new AdminNavbarBreadcrumbItem(this.activeProjectID, ['project',this.activeProjectID], true));
		 
		this.adminNavigationBarService.setAdminBreadCrumbNavigation(x);
	}

}
