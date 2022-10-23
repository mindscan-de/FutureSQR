import { Injectable } from '@angular/core';

// proxy to other service.
import { NavigationBarService} from '../../services/navigation-bar.service'
import { NavbarBreadcrumbItem } from '../../services/model/navbar-breadcrumb-item';

@Injectable({
  providedIn: 'root'
})
export class AdminNavigationBarService {

 	constructor(
		private navigationBarService:NavigationBarService
	) {
		
	}
	
	private setBreadCrumbNavigation(navBarItems:NavbarBreadcrumbItem[]):void {
		this.navigationBarService.setBreadcrumbNavigation(navBarItems);
	}
	
	public setAdminBreadCrumbNavigation(adminNavBarItems):void {
		let translated:NavbarBreadcrumbItem[] = [];
		// prepend with common configuration link...
		translated.push(new NavbarBreadcrumbItem('configuration',['/', 'admin'], false));
		
		// translate admin bar items to breadcrumb links
		
		this.setBreadCrumbNavigation(translated);
	}
	
	public clearBreadcrumbNavigation():void {
		this.navigationBarService.clearBreadcrumbNavigation();
	}
}
