import { Injectable } from '@angular/core';

// proxy to other service.
import { NavigationBarService} from '../../uiservices/navigation-bar.service'
import { NavbarBreadcrumbItem } from '../../uiservices/model/navbar-breadcrumb-item';

// 
import { AdminNavbarBreadcrumbItem } from './model/admin-navbar-breadcrumb-item';

@Injectable({
  providedIn: 'root'
})
export class AdminNavigationBarService {
	
	private admin_link:string[] = ['/', 'admin'];

 	constructor(
		private navigationBarService:NavigationBarService
	) {
	}
	
	private setBreadCrumbNavigation(navBarItems:NavbarBreadcrumbItem[]):void {
		this.navigationBarService.setBreadcrumbNavigation(navBarItems);
	}
	
	public setAdminBreadCrumbNavigation(adminNavBarItems:AdminNavbarBreadcrumbItem[]):void {
		let translated:NavbarBreadcrumbItem[] = [];
		
		// prepend with admin configuration link...
		translated.push(new NavbarBreadcrumbItem('configuration',this.admin_link, false));
		
		// translate admin bar items to breadcrumb links
		for(let i: number = 0; i<adminNavBarItems.length;i++) {
			translated.push(this.translateToNav(adminNavBarItems[i]));
		}
		
		this.setBreadCrumbNavigation(translated);
	}
	
	private translateToNav(adminNavBarItem:AdminNavbarBreadcrumbItem) : NavbarBreadcrumbItem {
		let navLinkText:string = adminNavBarItem.linkText;
		let navLinkActive:boolean = adminNavBarItem.routerLinkActive;
		let navRouterLink:any[] = [];
		
		// prepend new nav router link
		navRouterLink = navRouterLink.concat(this.admin_link);
		
		// navRouterLink.append
		navRouterLink = navRouterLink.concat(adminNavBarItem.routerLink);
		
		// append original data
		let translatedLink : NavbarBreadcrumbItem = new NavbarBreadcrumbItem(navLinkText,navRouterLink,navLinkActive ); 
		
		return translatedLink;
	}
	
	public clearBreadcrumbNavigation():void {
		this.navigationBarService.clearBreadcrumbNavigation();
	}
}
