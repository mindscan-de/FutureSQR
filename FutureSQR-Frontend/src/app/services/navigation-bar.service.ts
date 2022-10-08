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

	setBreadcrumbNavigation(navBarItems:NavbarBreadcrumbItem[]) {
		// update the currentNavbarItems / make a copy such that the values can not be tampered with....
		
		// update all the subscribed navbar listeners.
		this._currentNavbarSubject.next(navBarItems);
	}
}
