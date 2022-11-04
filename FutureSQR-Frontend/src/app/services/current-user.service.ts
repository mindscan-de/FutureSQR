import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

import { AuthNService } from '../authn/auth-n.service';

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
		
		this.authNService.liveBackendUserData().subscribe(
			updatedUserData => {
				
			},
		);
	}
	
		
	
	setCurrentUiUser(currentUiUser:CurrentUiUser):void {
		// update the backing item
		this._currentUserValue = currentUiUser;
		
		// update all subscribed current user Listeners
		this._currentUserSubject.next(this._currentUserValue);
	}
	
	
	getCurrentUserUUID():string {
		// TODO correct this later.
		return "8ce74ee9-48ff-3dde-b678-58a632887e31";
		// return this._currentUserValue.uuid;
	}
	
	
	clearCurrentUiUser() : void {
		this._currentUserValue = new CurrentUiUser();
		this._currentUserSubject.next(this._currentUserValue);
	}
	
	
	asObservable(): Observable<CurrentUiUser> {
		return this.currentUserSubject;
	}
}
