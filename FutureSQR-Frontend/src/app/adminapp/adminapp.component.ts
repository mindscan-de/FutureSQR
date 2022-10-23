import { Component, OnInit } from '@angular/core';

// Ganeral App Services
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
	) { }

	ngOnInit(): void {
		this.updateNavigationBar();
	}
	
    updateNavigationBar() {
		// add navigation
		let x = []
		x.push(new NavbarBreadcrumbItem( 'admin configuration', ['/','admin'], true ));
		this.navigationBarService.setBreadcrumbNavigation(x);		

    }

}
