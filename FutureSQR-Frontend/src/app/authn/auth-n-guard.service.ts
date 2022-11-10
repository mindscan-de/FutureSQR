import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { HttpXsrfTokenExtractor } from '@angular/common/http';

import { AuthNService } from './auth-n.service';

@Injectable({
  providedIn: 'root'
})
export class AuthNGuardService implements CanActivate {
	
	constructor(
		private authNService: AuthNService,
		private router: Router,
		private tokenExtraktor:HttpXsrfTokenExtractor
	) { }
	
	canActivate( route: ActivatedRouteSnapshot, state: RouterStateSnapshot ) {
		// if javascript lifecylce is authenticted and authentcation lifecycle is user logged in
		if(this.authNService.isAuthenticatedInCurrentLifecycle()) {
			return true;
		}

		// check we have a valid XSRF token for posting
		if (!this.tokenExtraktor.getToken()){
			return this.router.createUrlTree(['/','account','reauthws'], { queryParams: {returnUrl : state.url} });
		}

		// check first userlifecycle says that thelast known state was "logged in"
		if(!this.authNService.isUserLoggedIn()) {
			// user is not logged in, or is logged out
			return this.router.createUrlTree(['/','account','login'], { queryParams: {returnUrl : state.url} });
		}
		
		// let silently reauthenticate
		let isReauthenticatedPromise = this.authNService.reauthenticate({
			failed: () => {
					this.router.navigate(['/','account','login'], { queryParams: {returnUrl : state.url} });
				}
			});
			
		return isReauthenticatedPromise;
	}
}
