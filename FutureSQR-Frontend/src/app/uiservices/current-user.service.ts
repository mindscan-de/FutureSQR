import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

import { AuthNService } from '../authn/auth-n.service';

import { CurrentBackendUser } from '../authn/model/current-backend-user';
import { CurrentUiUser } from './model/current-ui-user';

@Injectable({
  providedIn: 'root'
})
export class CurrentUserService {

	private _currentUserValue:CurrentUiUser = new CurrentUiUser();
	
	private _currentUserSubject: BehaviorSubject<CurrentUiUser>;
	private currentUserSubject: Observable<CurrentUiUser>;

	constructor(
		private authNService : AuthNService
	) {
		this._currentUserSubject = new BehaviorSubject<CurrentUiUser>(this._currentUserValue);
		this.currentUserSubject = this._currentUserSubject.asObservable();
		
		this.authNService.liveBackendUserData().subscribe( {
			next: data => {
				this.setCurrentUiUser(this.m2mUserTransform(data));
			}
		});
	}
	
	m2mUserTransform(backendUser:CurrentBackendUser): CurrentUiUser {
		let uiUser = new CurrentUiUser();
		
		uiUser.displayName = backendUser.displayname || "";
		uiUser.logonName = backendUser.loginname || "";
		uiUser.uuid = backendUser.uuid || "";
		uiUser.avatar = backendUser.avatarlocation || "";
		
		return uiUser;
	}
	
	setCurrentUiUser(currentUiUser:CurrentUiUser):void {
		// update the backing item
		this._currentUserValue = currentUiUser;
		
		// update all subscribed current user Listeners
		this._currentUserSubject.next(this._currentUserValue);
	}
	
	getCurrentUserUUID():string {
		return this._currentUserValue.uuid;
	}
	
	clearCurrentUiUser() : void {
		this._currentUserValue = new CurrentUiUser();
		this._currentUserSubject.next(this._currentUserValue);
	}
	
	liveUiUserData(): Observable<CurrentUiUser> {
		return this.currentUserSubject;
	}
}
