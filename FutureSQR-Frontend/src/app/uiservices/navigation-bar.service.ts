import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

import { NavbarBreadcrumbItem } from './model/navbar-breadcrumb-item';

/**
* 
* The idea behind this service is to provide a way to update the top navigation bar with a breadcrumb navigation.
*
*/

@Injectable({
  providedIn: 'root'
})
export class NavigationBarService {
	
	// private property which is backing the current navigation bar.
	private _currentNavbarItems:NavbarBreadcrumbItem[] = [];
	
	// public item where we keep the subscriptons
	private _currentNavbarSubject: BehaviorSubject<NavbarBreadcrumbItem[]>;
	public currentNavbarSubject: Observable<NavbarBreadcrumbItem[]>;

	constructor() {
		this._currentNavbarSubject = new BehaviorSubject<NavbarBreadcrumbItem[]>(this._currentNavbarItems);
		this.currentNavbarSubject = this._currentNavbarSubject.asObservable();
	}

	/**
	 * This should actually be handled by some kind of navigation graph, where we can push and pop and
     * just mark the transitions and the navigationbar will know what to display. and each component
     * knows for itself what to add to the navigation bar, currently this is a very rude concept.
	 */
	public setBreadcrumbNavigation(navBarItems:NavbarBreadcrumbItem[]):void {
		// update the currentNavbarItems / make a copy such that the values can not be tampered with....
		this._currentNavbarItems = navBarItems;

		// update all the subscribed navbar listeners.
		this._currentNavbarSubject.next(this._currentNavbarItems);
	}
	
	public createItem(linkText:string, routerLink:any[], routerLinkActive: boolean): NavbarBreadcrumbItem {
		return new NavbarBreadcrumbItem(linkText, routerLink, routerLinkActive);
	}
	
	public clearBreadcrumbNavigation():void {
		this._currentNavbarItems = [];
		this._currentNavbarSubject.next(this._currentNavbarItems);
	}
	
	// todo provide an object for subscription.
	public asObservable():Observable<NavbarBreadcrumbItem[]> {
		return this.currentNavbarSubject;
	}
}
