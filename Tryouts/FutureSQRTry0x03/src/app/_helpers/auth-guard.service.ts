import { Injectable } from '@angular/core';
import {Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';

import { AccountService } from '../_services/account.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{
	
	constructor(
		private router: Router,
		private accountService: AccountService
	) { }
	
	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		// so we are either logged in
		const user = this.accountService.userValue;
		
		if( user ) {
			return true;
		}
		
		// if we don't have a logged in user, we want the user to navigate to the 
		// account page and perform a login using a return URL
		
		// redirect to the login page.
		this.router.navigate(['/account/login'], { queryParams: { returnUrl : state.url}});
		return false; 
	}
	
}
