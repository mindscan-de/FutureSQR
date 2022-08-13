import { Injectable } from '@angular/core';
import {Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{
	
	constructor(
		private router: Router
	) { }
	
	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		// TODO: check if we have a user
		
		// if we don't have a user, we want the user to navigate to the account page and perform a login
		// using a return URL
	
		return false; 
	}
	
}
