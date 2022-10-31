import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthNService {
	
	private static readonly URL_LOGIN_AUTHENTICATE    = "/FutureSQR/rest/user/authenticate";
	private static readonly URL_LOGIN_REAUTHENTICATE  = "/FutureSQR/rest/user/reauthenticate";
	private static readonly URL_LOGOUT                = "/FutureSQR/rest/user/logout";
	
	// Actually we have to deal with two different life cycles
	
	
	// Javascript lifecycle (e.g. F5 (pagereload) or bookmarks)
	// Authentication lifecycle, which can span multiple javascript lifecycles
	
	constructor(
		private httpClient : HttpClient		
	) { }
	
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
	reauthenticate( ): void {
	}
	
	async reAuthenticateAsync():Promise<any> {
		let url = AuthNService.URL_LOGIN_REAUTHENTICATE;
		
		return await this.httpClient.get<any>(url, {}).toPromise();
	}
	
	
	logout():void {
		// clear userdata
		// clear authorizatuin data
		// autnguard should be informed...?
		// maybe userdata via subscription?
		
		// we should persist that the authentication lifecylcle was ""logged out""
	}
	
	isAuthenticatedInCurrentLifecycle() : boolean {
		// We must answer the question, whether we did an authentication within the current
		// Java script lifecylce
		
		// we return true iff:
		// * either successful login, 
		// * or a successful reauthentication
		// we return false iff:
		// * the user logged out in this lifecycle
		

		return true;
	}
}
