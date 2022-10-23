import { Component, OnInit } from '@angular/core';

// General App Services
import { NavigationBarService } from '../services/navigation-bar.service';
import { NavbarBreadcrumbItem } from '../services/model/navbar-breadcrumb-item';


@Component({
  selector: 'app-adminapp',
  templateUrl: './adminapp.component.html',
  styleUrls: ['./adminapp.component.css']
})
export class AdminappComponent implements OnInit {

	constructor(
		private navigationBarService : NavigationBarService
	) { 
		// TODO: register navigation bar proxy access 		
	}

	ngOnInit(): void {
		this.updateNavigationBar();
	}
	
    updateNavigationBar() {
		// add navigation
		let x = []
		x.push(new NavbarBreadcrumbItem( 'configuration', ['/','admin'], true ));
		this.navigationBarService.setBreadcrumbNavigation(x);		

    }

}
