import { Component, OnInit } from '@angular/core';

// General App Services
import { NavbarBreadcrumbItem } from '../services/model/navbar-breadcrumb-item';

// Admin App services
import { AdminNavigationBarService } from './services/admin-navigation-bar.service';

@Component({
  selector: 'app-adminapp',
  templateUrl: './adminapp.component.html',
  styleUrls: ['./adminapp.component.css']
})
export class AdminappComponent implements OnInit {

	constructor(
		private adminNavBarService : AdminNavigationBarService
	) { 
		// TODO: register navigation bar proxy access 		
	}

	ngOnInit(): void {
		this.updateNavigationBar();
	}
	
    updateNavigationBar() {
		// add navigation
		let x = []
		this.adminNavBarService.setAdminBreadCrumbNavigation(x);		
    }

}
