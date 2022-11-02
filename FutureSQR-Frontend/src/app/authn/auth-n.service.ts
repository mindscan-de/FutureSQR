import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpSentEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { map, first, tap } from 'rxjs/operators';
import { BackendModelSimpleUserEntry } from '../backend/model/backend-model-simple-user-entry';


@Injectable({
	providedIn: 'root'
})
export class AuthNService {

	private static readonly URL_LOGIN_AUTHENTICATE = "/FutureSQR/rest/user/authenticate";
	private static readonly URL_LOGIN_REAUTHENTICATE = "/FutureSQR/rest/user/reauthenticate";
	private static readonly URL_LOGOUT = "/FutureSQR/rest/user/logout";


	private lastUpdate?: Date;
	private userInfo?: AuthUserInfo;


	// Actually we have to deal with two different life cycles
	// Javascript lifecycle (e.g. F5 (pagereload) or bookmarks)
	// Authentication lifecycle, which can span multiple javascript lifecycles

	constructor(
		private httpClient: HttpClient
	) {
		console.info("initial get current user")
		this.reauthenticateSilent();
	}

	// this will do a full login using username and password.
	login(loginname: string, password: string, callbacks: LoginCallback): void {

		// TODO: maybe we need to aquire a authentication crsf token to send our login
		//       maybe later.

		let formData = new FormData();

		formData.set('loginname', loginname);
		formData.set('password', password);

		// TDOO: Use Backend to send password and username for authentication
		this.httpClient.post<AuthUserInfo>(AuthNService.URL_LOGIN_AUTHENTICATE, formData).pipe(first()).subscribe(
			{
				next: data => {
					// TODO on logindata received
					// check the login data
					// on success
					// deploy userdata
					// deploy authorization data

					// parse the userdata for user data and authorization information.  
					// receive authorization and userdata
				},
				error: e => {
					// TODO on login failed for reasons
				}
			}
		);

	}

	// TODO silent reauthentication e.g. on reload of the page, we need to retrieve the authn and authz data again, 
	// if that fails we have to inform someone, that the silent reauthentication did not worked, and the login page
	// can be presented instead.
	// on success we deploy userdata
	// on success we deploy authoritzation data
	// on success we inform authnguard via callback or so.
	reauthenticateSilent(): void {
		this.reAuthenticate().subscribe();
	}

	reAuthenticate(): Observable<AuthUserInfo | null> {
		let url = AuthNService.URL_LOGIN_REAUTHENTICATE;

		return this.httpClient.get<AuthUserInfo | null>(url, {}).pipe(first(),
			tap<AuthUserInfo>({ next: this.processNewUserData, error: console.error, complete: () => console.info("Reauth completed.") }));
	}

	processNewUserData(user: AuthUserInfo) {
		console.info(user);
		if (user instanceof AuthUserInfo) {
			this.lastUpdate = new Date();
			this.userInfo = user;
		} else {
			console.error("Unexpected answer type: ", typeof user)
		}
	}

	logout(): void {

		// clear userdata
		localStorage.clear()
		// clear authorizatuin data
		this.lastUpdate = undefined;
		// authz should be informed...?
		// maybe userdata via subscription?
		this.httpClient.post(AuthNService.URL_LOGOUT, null)
		// we should persist that the authentication lifecylcle was ""logged out""
	}

	isAuthenticatedInCurrentLifecycle(): boolean | Promise<boolean> {
		// We must answer the question, whether we did an authentication within the current
		// Java script lifecylce

		// we return true iff:
		// * either successful login, 
		// * or a successful reauthentication
		// we return false iff:
		// * the user logged out in this lifecycle


		return this.userInfo !== undefined;
	}
}

export class LoginCallback {
	success?: (AuthUserInfo) => void
	fail?: () => void
	error?: () => void
}

export class AuthUserInfo extends BackendModelSimpleUserEntry {

	roles: string[] = []
}
