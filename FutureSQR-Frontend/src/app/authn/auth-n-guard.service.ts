import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { AuthNService } from './auth-n.service';
import { CurrentBackendUser } from './model/current-backend-user';
import { CurrentBackendUserUtils } from './model/current-backend-user-utils';

@Injectable({
  providedIn: 'root'
})
export class AuthNGuardService implements CanActivate {
	
	private backendUserUtils = new CurrentBackendUserUtils();

	constructor(
		private authNService: AuthNService,
		private router: Router
	) { }
	
	canActivate( route: ActivatedRouteSnapshot, state: RouterStateSnapshot ) {
		// We either return true, or false
		// We return a promise, when we ask for reauthentication (success / fail....)
		
		// if javascript lifecylce is authenticted and authentcation lifecycle is user logged in
		if(this.authNService.isAuthenticatedInCurrentLifecycle()) {
			return true;
		}

		// check first userlifecycle says that thelast known state was "logged in"
		if(this.authNService.isUserLoggedIn()) {
			
			// let silently reauthenticate
			let isReauthenticatedPromise = this.authNService.reauthenticate({
				failed: () => {
						this.router.navigate(['/','account','login'], { queryParams: {returnUrl : state.url} });
					}
				});
				
			return isReauthenticatedPromise;
		}
		
		// user is not logged in, or is logged out
		this.router.navigate(['/','account','login'], { queryParams: {returnUrl : state.url} });
		return false;
	}
}
