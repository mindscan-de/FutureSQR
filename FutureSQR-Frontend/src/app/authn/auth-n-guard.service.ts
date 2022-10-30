import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { AuthNService } from './auth-n.service';

@Injectable({
  providedIn: 'root'
})
export class AuthNGuardService implements CanActivate {

	constructor(
		private authNService: AuthNService,
		private router: Router
	) { }
	
	canActivate( route: ActivatedRouteSnapshot, state: RouterStateSnapshot ) {
		// We either return true, or false
		// we return true, if the user is correctly authenticated
		
		// if javascript lifecylce is authenticted and authentcation lifecycle is user logged in
		if(this.authNService.isAuthenticatedInCurrentLifecycle()) {
			return true;
		}

		// if javascript lifecycle is new and authentication lifecycle is logged in user
		// we want to reauthenticate - send the user to a waiting page (return false), and 
		// if user could be reauthenicated, and send him back to the url he actually wanted 
		// to go, when the reauthenication was performed successfully.
		
		// if the user lifecycle is not logged in, we return false, and route to the login page, 
		// when the user is not properly authenticated 
		//  this.router.navigate(['/','account','login'], { queryParams: {returnUrl : state.url} });
		// return false;
		
		return true;
	}
}
