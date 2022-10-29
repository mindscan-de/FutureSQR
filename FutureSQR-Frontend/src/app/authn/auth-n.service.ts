import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthNService {
	
	constructor() { }
	
	// this will do a full login.
	login( loginname: string, password: string, callbacks ):void {
		// TDOO: Use Backend to send password and username for authentication
		// receive authorization and userdata
		// deploy userdata
		// deploy authorization data  
	}
	
	// TODO silent reauthentication e.g. on reload of the page, we need to retrieve the authn and authz data again, 
	// if that fails we have to inform someone, that the silent reauthentication did not worked, and the login page
	// can be presented instead.
	// on success we deploy userdata
	// on success we deploy authoritzation data
	// on success we inform authnguard via callback or so.
}
