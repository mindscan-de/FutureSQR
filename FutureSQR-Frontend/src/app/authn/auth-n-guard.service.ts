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
		// we return true, if the user is correctly authenticated
		
		// if javascript lifecylce is authenticted and authentcation lifecycle is user logged in
		if(this.authNService.isAuthenticatedInCurrentLifecycle()) {
			return true;
		}

		// check first userlifecycle says that thelast known state was "logged in"
		if(this.authNService.isUserLoggedIn()) {
			
			// if javascript lifecycle is new  we want to reauthenticate - and maybe send the 
			// user to a waiting page (return false), and 
			// if user could be reauthenicated, and send him back to the url he actually wanted 
			// to go, when the reauthenication was performed successfully. If the reauthenication 
			// says no, we must send the user to the login page using this current state url.
			let loginResult : boolean = false;
			
			console.log("canactivate incoking reauthenticate async");
			
			// ------------------
			// This is very likely not the right solution, because some of the details
			// like distributing the backend user data is the job of authnservice.
			// ------------------
			let foo:Promise<CurrentBackendUser> = this.authNService.reAuthenticateAsync();
			foo.then( user => {
				console.log("this is the user data we got:");
				console.log(user);
				if(this.backendUserUtils.isValid(user)) {
					console.log("we actually have a valid user.... for reauthenticate....");
					loginResult = true;
					// TODO: remember that the authNservice must distribute reauthenticated user.
					// TODO:  
				}
				else
				{
					loginResult = false;
					// TODO: or clear the current user data, becuase the user could not be reauthenticated.
				}
			});
			
			// TODO: auf das ergebnis des Promise warten....
			
			if (!loginResult) {
				// this is not a user any more from the servers pov, therefore
				// this user must login
				console.log("let's send them to login because reauthenticate failed.");
				this.router.navigate(['/','account','login'], { queryParams: {returnUrl : state.url} });
			}
			
			return loginResult;
		}
		else
		{
			// user is not logged in, or is logged out
			this.router.navigate(['/','account','login'], { queryParams: {returnUrl : state.url} });
			return false;
		}
		
		// not reachable from implementation as for now speaking.
		return true;
	}
}
