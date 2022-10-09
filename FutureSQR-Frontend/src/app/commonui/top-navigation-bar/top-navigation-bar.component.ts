import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
 
import { UserDataQueryBackendService } from '../../backend/services/user-data-query-backend.service';

import { NavigationBarService } from '../../services/navigation-bar.service';
import { NavbarBreadcrumbItem } from '../../services/model/navbar-breadcrumb-item';


@Component({
  selector: 'app-top-navigation-bar',
  templateUrl: './top-navigation-bar.component.html',
  styleUrls: ['./top-navigation-bar.component.css']
})
export class TopNavigationBarComponent implements OnInit {

	public title:String = "APPTITLE";
	
	public navItems: Array<NavbarBreadcrumbItem> = new Array<NavbarBreadcrumbItem>();

	@Input() appTitle:string;
	
	constructor (
		private navigationBarService : NavigationBarService,
		private userDataService : UserDataQueryBackendService
	) {}

	ngOnInit(): void {
		// we want to subscribe to changes intended for the breadcrumb navigation
		this.navigationBarService.asObservable().subscribe(
			navbardata => { 
				this.onBreadCrumbNavChanged(navbardata);
			}
		);
		
		// TODO: we also want to subscribe to changes of the user, such that we can update 
		//       TopNavigationBar, when a user is logged in / logged out.
	}
	
	onBreadCrumbNavChanged(newBreadCrumbNavData:NavbarBreadcrumbItem[]) : void {
		// update the breadcrumb navigation
		this.navItems = new Array<NavbarBreadcrumbItem>();
		
		for(let i:number =0;i<newBreadCrumbNavData.length;i++) {
			let x:NavbarBreadcrumbItem = newBreadCrumbNavData[i];
			this.navItems.push(x);
		}
		
		// TODO handle empty case
		// TODO handle array case
	}
	
	onUserChanged(userData) : void {
		// TODO: detect if logged in or logged out
		// then clear state or update state what to show in the User part of the top navigation 
	}
	
	
	
	ngOnChanges(changes: SimpleChanges): void {
		let newTitle:string = changes.appTitle.currentValue;
		
		if(changes.appTitle.currentValue) {
			this.title = newTitle;
		}
	}

	
}
