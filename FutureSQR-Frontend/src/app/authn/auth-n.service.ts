import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { map, first } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class AuthNService {
	// URL to visit to retrieve a preauth token, to be able to post to AUTHENTICATE / REAUTHENTICATE
	private static readonly URL_PREAUTH_TOKEN          = "/FutureSQR/rest/user/preauthtoken";
	
	private static readonly URL_LOGIN_AUTHENTICATE    = "/FutureSQR/rest/user/authenticate";
	private static readonly URL_LOGIN_REAUTHENTICATE  = "/FutureSQR/rest/user/reauthenticate";
	private static readonly URL_LOGOUT                = "/FutureSQR/rest/user/logout";
	
	// Actually we have to deal with two different life cycles
	
	
	// Javascript lifecycle (e.g. F5 (pagereload) or bookmarks)
	// Authentication lifecycle, which can span multiple javascript lifecycles
	
	constructor(
		private httpClient : HttpClient		
	) { }
	
	// this will do a full login using username and password.
	login( loginname: string, password: string, callbacks ):void {
		
		// TODO: maybe we need to aquire a authentication crsf token to send our login
		//       maybe later.
		
		let formData = new FormData();
		
		formData.set('loginname', loginname);
		formData.set('password', password);
		// TODO also present: some kind of pre auth crsf tokens?
		
		let that = this;
		
		// TDOO: Use Backend to send password and username for authentication
		this.httpClient.post<any>(AuthNService.URL_LOGIN_AUTHENTICATE, formData).pipe(first()).subscribe(
			data => {
				that.loginOnDataReceived(data);
			},
			error => {
				that.loginOnError();
			}			
		);
	}
	
	loginOnDataReceived(data:any):  void {
		// check the login data		
		
		// Okay we got some data,
		// -- this can say, that the authentication failed for some reason, 
		//    -- then we reset the present authn and authz data?
		
		// or we got some user data, this needs to be set and distributed...
		// receive authorization and userdata
		// parse the userdata for user data and authorization information.  
		// deploy userdata
		// deploy authorization data
 
	}
	
	loginOnError(): void {
		// we received some error code from the server, so what to do in this case?
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
