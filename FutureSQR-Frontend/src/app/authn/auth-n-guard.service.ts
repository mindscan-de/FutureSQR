import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthNGuardService implements CanActivate {

	constructor(
		private router: Router		
	) { }
	
	canActivate( route: ActivatedRouteSnapshot, state: RouterStateSnapshot ) {
		// We either return true, or false
		// we return true, if the user is correctly authenticated
		
		// todo magic here....
		
		
		// we return false, and route to the login page, when the user is not properly authenticated.
		// this.router.navigate(['/','account','login'], { queryParams: {returnUrl : state.url} });
		// return false;
		
		return true;
	}
}
