import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, first } from 'rxjs/operators';

import { BrowserAuthLifecycleState } from './browser-auth-lifecycle-state.enum';
import { UserAuthLifecycleState } from './user-auth-lifecycle-state.enum';

import { CurrentBackendUser } from './model/current-backend-user';

@Injectable({
  providedIn: 'root'
})
export class AuthNService {
	
	// URL to visit to retrieve a preauth token, to be able to post to AUTHENTICATE / REAUTHENTICATE
	private static readonly URL_PREAUTH_TOKEN          = "/FutureSQR/rest/user/preauthtoken";
	
	private static readonly URL_LOGIN_AUTHENTICATE    = "/FutureSQR/rest/user/authenticate";
	private static readonly URL_LOGIN_REAUTHENTICATE  = "/FutureSQR/rest/user/reauthenticate";
	private static readonly URL_LOGOUT                = "/FutureSQR/rest/user/logout";
	
	// LOCALSTORAGE KEY for the current user authentication lifecycle state
	private static readonly LS_KEY_CURRENT_USER_AUTH_LIFECYCLE_STATE = "currentUserAuthLifecycleState";
	// TODO: LOCALSTORAGE KEY for the current user authentication lifecylce state
	private static readonly LS_KEY_CURRENT_BACKEND_USER_VALUE = "currentBackendUserValue";
	// TODO: LOCALSTORARE KEY for previous user
	private static readonly LS_KEY_PREVIOUS_BACKEND_USER_VALUE = "previousBackendUserValue";
	
	// Actually we have to deal with two different life cycles
	// this one only lives as a variable in memory
	// Javascript lifecycle (e.g. F5 (pagereload) or bookmarks)
	private __currentBrowserAuthLifeCycleState: BrowserAuthLifecycleState;
	// Authentication lifecycle, which can span multiple javascript lifecycles
	// this one lives in the localstorage
	private __currentUserAuthLifecycleState: UserAuthLifecycleState;
	
	// We will keep a copy od the current Backend user in AuthNService.
	private _currentBackendUserValue = new CurrentBackendUser();
	private _currentBackendUserSubject: BehaviorSubject<CurrentBackendUser>;
	private currentBackendUserSubject: Observable<CurrentBackendUser>;
	
	constructor(
		private httpClient : HttpClient		
	) { 
		this._currentBackendUserSubject = new BehaviorSubject<any>(this._currentBackendUserValue);
		this.currentBackendUserSubject = this._currentBackendUserSubject.asObservable();
		
		this.updateBrowserAuthLifecylceState( BrowserAuthLifecycleState.None);
		this.updateUserAuthLifecycleState(
			JSON.parse(localStorage.getItem(AuthNService.LS_KEY_CURRENT_USER_AUTH_LIFECYCLE_STATE)) 
			|| UserAuthLifecycleState.None);
	}
	
	// this will do a full login using username and password.
	login( loginname: string, password: string, callbacks ):void {
		// TODO: maybe we need to aquire a pre-auth authentication crsf token to send our login
		//       maybe later.
		// we only need to aquire this token, if we are in __currentBrowserAuthLifeCycleState None
		// we have this pre-auth token, if we are at __currentBrowserAuthLifeCycleState PreAuthenticated

		
		// a login attempt will kill the previous lifecylce state
		if(this.isUserLoggedIn()) {
			this.updateUserAuthLifecycleState(UserAuthLifecycleState.None);
		}
		
		let formData = new FormData();
		
		formData.set('username', loginname);
		formData.set('password', password);
		// TODO also present: some kind of pre auth crsf tokens?
		
		let that = this;
		
		// TDOO: Use Backend to send password and username for authentication
		this.httpClient.post<any>(AuthNService.URL_LOGIN_AUTHENTICATE, formData).pipe(first()).subscribe(
			data => {
				that.loginOnDataReceived(data);
				if(callbacks.next != undefined) {
					callbacks.next();
				}
			},
			error => {
				that.loginOnError();
				if(callbacks.error != undefined) {
					callbacks.error();
				}
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
	
		this.updateUserAuthLifecycleState(UserAuthLifecycleState.LoggedIn);
		this.updateBrowserAuthLifecylceState(BrowserAuthLifecycleState.FullyAuthenticated);
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
		let formData:FormData = new FormData();
		// TODO: fixme: Hardcoded username
		formData.set( 'username', 'mindscan-de'); 
		
		this.httpClient.post<any>(AuthNService.URL_LOGOUT, formData).pipe(first()).subscribe(
			data => {
				// enforce Navigate 
			},
			error => {}
		);
		
		this.updateBrowserAuthLifecylceState( BrowserAuthLifecycleState.None );
		this.updateUserAuthLifecycleState( UserAuthLifecycleState.LoggedOut );
		
		// TODO: autnguard should be informed...?
		// TODO: userdata via subscriptions should be informed?
		
		// TODO: we should persist that the user authentication lifecylcle was ""logged out""
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
	
	
	liveBackendUserData():Observable<CurrentBackendUser> {
		return this.currentBackendUserSubject;
	}
	
	
	private updateUserAuthLifecycleState(newState: UserAuthLifecycleState): void {
		this.__currentUserAuthLifecycleState = newState;
		
		localStorage.setItem(AuthNService.LS_KEY_CURRENT_USER_AUTH_LIFECYCLE_STATE, JSON.stringify(this.__currentUserAuthLifecycleState))
	}
	
	private updateBrowserAuthLifecylceState(newState: BrowserAuthLifecycleState) : void {
		this.__currentBrowserAuthLifeCycleState = newState;
	}
	
}
