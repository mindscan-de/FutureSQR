import { Component, OnInit, Input, SimpleChanges } from '@angular/core';

import { UserDataQueryBackendService } from '../../backend/services/user-data-query-backend.service';

import { NavigationBarService } from '../../services/navigation-bar.service';
import { NavbarBreadcrumbItem } from '../../services/model/navbar-breadcrumb-item';

import { CurrentUserService } from '../../services/current-user.service';
import { CurrentUiUser } from '../../services/model/current-ui-user';

import { AuthZService } from '../../authz/auth-z.service';
import { CurrentAuthorizations } from '../../authz/model/current-authorizations';

@Component({
  selector: 'app-top-navigation-bar',
  templateUrl: './top-navigation-bar.component.html',
  styleUrls: ['./top-navigation-bar.component.css']
})
export class TopNavigationBarComponent implements OnInit {

	public title:String = "APPTITLE";
	public currentUserUnknown:boolean = true;
	public currentUserIsAdmin:boolean = false;
	
	public navItems: Array<NavbarBreadcrumbItem> = new Array<NavbarBreadcrumbItem>();

	@Input() appTitle:string;
	
	constructor (
		private navigationBarService : NavigationBarService,
		private currentUserService : CurrentUserService,
		private authZService : AuthZService,
		private userDataService : UserDataQueryBackendService
	) {}

	ngOnInit(): void {
		// we want to subscribe to changes intended for the breadcrumb navigation
		this.navigationBarService.asObservable().subscribe(
			navbardata => { 
				this.onBreadCrumbNavChanged(navbardata);
			}
		);
		
		// we also want to subscribe to changes of the user, such that we can update 
		// TopNavigationBar, when a user is logged in / logged out
		this.currentUserService.asObservable().subscribe(
			currentuser => {
				this.onCurrentUiUserChanged(currentuser);
			}
		);
		
		this.authZService.asObservable().subscribe(
			currentAuthZValues => {
				this.onCurrentAuthZChanged(currentAuthZValues);
			} 
		)
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
	
	
	
	onCurrentUiUserChanged(currentUser:CurrentUiUser) : void {
		// if current user is anonymous, then the user info ist not present
		this.currentUserUnknown = currentUser.isAnonymous();
		
		// TODO: detect if logged in or logged out
		// then clear state or update state what to show in the User part of the top navigation 
	}
	
	onCurrentAuthZChanged(currentAuthZ:CurrentAuthorizations) : void {
		this.currentUserIsAdmin = currentAuthZ.isAdmin;
	}
	
	ngOnChanges(changes: SimpleChanges): void {
		let newTitle:string = changes.appTitle.currentValue;
		
		if(changes.appTitle.currentValue) {
			this.title = newTitle;
		}
	}

	// this is a fake login until we have the correct login logic implemented.
	fakeLoginRemoveMe(): void {
		/*
		 * The whole method should not be here and must be removed, when we actually have a real login.
	     * For the moment i want to develop the top navigation bar, and the ui user service.
         * This ui user should be set in the currentUserService, by a login mechanism, not from here.
		 */
		let newUiUser = new CurrentUiUser();
		newUiUser.logonName = "mindscan-de";
		newUiUser.uuid = "b4d1449b-d50e-4c9f-a4cb-dd2230278306";
		newUiUser.displayName = "Maxim Gansert";
		
		this.currentUserService.setCurrentUiUser(newUiUser);
	}
	
	fakeAdminRemoveMe(): void {
		this.authZService.setFakeAdmin();
	}
	
	onLogoutClicked():void {
		// TODO invoke backend logout, and on success of backend logout, the currentUserService receives a new User...
		// This here is just to satisfy the ui for now and communicate the idea.
		
		let newUiUser = new CurrentUiUser();
		newUiUser.uuid = "b4d1449b-d50e-4c9f-a4cb-dd2230278306";
		
		this.currentUserService.setCurrentUiUser(newUiUser);
		this.authZService.clearFakeAdmin();
	}
}
