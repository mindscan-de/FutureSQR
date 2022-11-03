import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { map, first } from 'rxjs/operators';

import { BrowserAuthLifecycleState } from './browser-auth-lifecycle-state.enum';
import { UserAuthLifecycleState } from './user-auth-lifecycle-state.enum';

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
	// this one only lives as a variable in memory
	// Javascript lifecycle (e.g. F5 (pagereload) or bookmarks)
	private __currentBrowserAuthLifeCycleState: BrowserAuthLifecycleState;
	// Authentication lifecycle, which can span multiple javascript lifecycles
	// this one lives in the localstorage
	private __currentUserAuthLifecycleState: UserAuthLifecycleState;
	
	
	constructor(
		private httpClient : HttpClient		
	) { 
		this.__currentBrowserAuthLifeCycleState = BrowserAuthLifecycleState.None;
		
		// actually we should retrieve this from localstorage, and if not available then assume None
		this.__currentUserAuthLifecycleState= UserAuthLifecycleState.None;
		
		
		// TODO: And we should keep this information in a BehaviorSubject, 
		//       such that these things can be subscribed to if needed 
	}
	
	// this will do a full login using username and password.
	login( loginname: string, password: string, callbacks ):void {
		
		
		// TODO: maybe we need to aquire a pre-auth authentication crsf token to send our login
		//       maybe later.
		// we only need to aquire this token, if we are in __currentBrowserAuthLifeCycleState None
		// we have this pre-auth token, if we are at __currentBrowserAuthLifeCycleState PreAuthenticated
		
		// TODO: also we should here clear the __currentUserAuthLifecycleState To None if Not set to none.
		
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
		// TODO: set currentUserAuthLifeCycleState to UserAuthLifecycleState.None
		
		// or we got some user data, this needs to be set and distributed...
		// receive authorization and userdata
		// parse the userdata for user data and authorization information.  
		// deploy userdata
		// deploy authorization data
 		// TODO: set currentUserAuthLifeCycleState to UserAuthLifecycleState.LoggedIn

	}
	
	loginOnError(): void {
		// we received some error code from the server, so what to do in this case?
		
		// TODO: set currentUserAuthLifeCycleState to UserAuthLifecycleState.None
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
		if(this.__currentUserAuthLifecycleState == UserAuthLifecycleState.LoggedIn) {
			if(	(this.__currentBrowserAuthLifeCycleState === BrowserAuthLifecycleState.FullyAuthenticated) ||
				(this.__currentBrowserAuthLifeCycleState === BrowserAuthLifecycleState.ReAuthenticated) ) {
				return true;
			}
		}

		return false;
	}
	
    isUserLoggedIn() {
        return this.__currentUserAuthLifecycleState == UserAuthLifecycleState.LoggedIn;
    }
	
}
