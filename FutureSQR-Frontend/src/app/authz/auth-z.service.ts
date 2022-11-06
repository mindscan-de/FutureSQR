import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

import { AuthNService } from '../authn/auth-n.service';

import { CurrentAuthorizations } from './model/current-authorizations';
import { CurrentBackendUser } from '../authn/model/current-backend-user';

@Injectable({
  providedIn: 'root'
})
export class AuthZService {
	
	// This service is only for components which need to be aware of changes of the user or his rights 
	// (such as e.g. the top-navigation-bar)
	
	private _currentAuthorizationsValue:CurrentAuthorizations = new CurrentAuthorizations();
	
	private _currentAuthorizationsSubject: BehaviorSubject<CurrentAuthorizations>;
	private currentAuthorizationsSubject: Observable<CurrentAuthorizations>;

	constructor(
		private authNService : AuthNService
	) { 
		this._currentAuthorizationsSubject = new BehaviorSubject<CurrentAuthorizations>(this._currentAuthorizationsValue);
		this.currentAuthorizationsSubject = this._currentAuthorizationsSubject.asObservable();
		
		this.authNService.liveBackendUserData().subscribe({
			next: data => {
				this.setCurrentPrivileges(this.m2mUserTransform(data));
			},
		});
	}
	
	m2mUserTransform(backendUser:CurrentBackendUser): CurrentAuthorizations {
		let newPrivileges = new CurrentAuthorizations();
		if(backendUser.capabilities != undefined) {
			newPrivileges.isAdmin = backendUser.capabilities.includes('admin');
		}
		return newPrivileges;
	}
	
	setCurrentPrivileges(privileges:CurrentAuthorizations ): void {
		this._currentAuthorizationsValue = privileges;
		this._currentAuthorizationsSubject.next(this._currentAuthorizationsValue);
	}
	
	// TODO: deprecated
	setFakeAdmin():void {
		let newAuthorizationValue:CurrentAuthorizations = new CurrentAuthorizations();
		newAuthorizationValue.isAdmin = true;
		this._currentAuthorizationsValue = newAuthorizationValue;
		this._currentAuthorizationsSubject.next(this._currentAuthorizationsValue);
	}
	
	// TODO: deprecated
	clearFakeAdmin():void {
		let newAuthorizationValue:CurrentAuthorizations = new CurrentAuthorizations();
		newAuthorizationValue.isAdmin = false;
		this._currentAuthorizationsValue = newAuthorizationValue;
		this._currentAuthorizationsSubject.next(this._currentAuthorizationsValue);
	}
	
	// TODO rename this to liveUserAuthorizations
	asObservable(): Observable<CurrentAuthorizations> {
		return this.currentAuthorizationsSubject;
	}
}
