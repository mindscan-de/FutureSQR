import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';

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
		// if javascript lifecylce is authenticted and authentcation lifecycle is user logged in
		if(this.authNService.isAuthenticatedInCurrentLifecycle()) {
			return true;
		}

		// check first userlifecycle says that thelast known state was "logged in"
		if(!this.authNService.isUserLoggedIn()) {
			// user is not logged in, or is logged out
			return this.getLoginUrl(state);
		}

		// let silently reauthenticate
		let isReauthenticatedPromise = this.authNService.reauthenticate();

		return isReauthenticatedPromise.then(
			isReauth => isReauth ? true : this.getLoginUrl(state),
			reason => {
				console.error(reason);
				return this.getLoginUrl(state)
			}
		);
	}

	private getLoginUrl(state: RouterStateSnapshot): UrlTree {
		return this.router.createUrlTree(['/', 'account', 'login'], { queryParams: { returnUrl: state.url } })
	}
}
