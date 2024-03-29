import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
import { Router, Route } from '@angular/router';

import { NavigationBarService } from '../../uiservices/navigation-bar.service';
import { NavbarBreadcrumbItem } from '../../uiservices/model/navbar-breadcrumb-item';

import { CurrentUserService } from '../../uiservices/current-user.service';
import { CurrentUiUser } from '../../uiservices/model/current-ui-user';

import { AuthNService } from '../../authn/auth-n.service';
import { AuthZService } from '../../authz/auth-z.service';
import { CurrentAuthorizations } from '../../authz/model/current-authorizations';

@Component({
  selector: 'app-top-navigation-bar',
  templateUrl: './top-navigation-bar.component.html',
  styleUrls: ['./top-navigation-bar.component.css']
})
export class TopNavigationBarComponent implements OnInit {

	public title:string = "APPTITLE";
	public currentUserUnknown:boolean = true;
	public currentUserIsAdmin:boolean = false;
	public avatarlocation:string= "";
	
	public navItems: Array<NavbarBreadcrumbItem> = new Array<NavbarBreadcrumbItem>();

	@Input() appTitle:string;
	
	constructor (
		private navigationBarService : NavigationBarService,
		private currentUserService : CurrentUserService,
		private authNService : AuthNService,
		private authZService : AuthZService,
		private router: Router
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
		this.currentUserService.liveUiUserData().subscribe(
			currentuser => {
				this.onCurrentUiUserChanged(currentuser);
			}
		);
		
		this.authZService.liveUserAuthorizations().subscribe(
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
		
		this.avatarlocation = currentUser.avatar;
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

	onLogoutClicked():void {
		this.authNService.logout({
				onlogout : () => {
					this.router.navigate(['/','account','redirect'], { queryParams: {redirectUrl : this.router.routerState.snapshot.url} });
				}
			}
		);
	}
}
