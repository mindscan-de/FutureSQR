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

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

		const login = this.authNService.isAuthenticatedInCurrentLifecycle()

		if (login instanceof Promise) {

			return new Promise<boolean | UrlTree>((resolve, reject) => {
				(login as Promise<boolean>)
					.then(
						(isLogin: boolean) => { resolve(this.handleBooleanLoginState(isLogin, state.url)) },
						(error) => { reject(error) })
			})
		}
		return this.handleBooleanLoginState((login as boolean), state.url);
	}

	private handleBooleanLoginState(isLogin: boolean, returnUrl: string): boolean | UrlTree {

		return isLogin ? true : this.router.createUrlTree(["account", "login"], { queryParams: { returnUrl: returnUrl } });
	}
}
