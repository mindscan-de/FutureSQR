import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

import { CurrentUiUser } from './model/current-ui-user';

@Injectable({
  providedIn: 'root'
})
export class CurrentUserService {

	private _currentUserValue:CurrentUiUser = new CurrentUiUser();
	
	private _currentUserSubject: BehaviorSubject<CurrentUiUser>;
	private currentUserSubject: Observable<CurrentUiUser>;

	constructor() {
		this._currentUserSubject = new BehaviorSubject<CurrentUiUser>(this._currentUserValue);
		this.currentUserSubject = this._currentUserSubject.asObservable();
	}
	
	
	setCurrentUser(currentUser):void {
		// update the backing item
		this._currentUserValue = currentUser;
		
		// update all subscribed current user Listeners
		this._currentUserSubject.next(this._currentUserValue);
	}
	
	
	clearCurrentUser() : void {
		this._currentUserValue = new CurrentUiUser();
		this._currentUserSubject.next(this._currentUserValue);
	}
	
	
	asObservable(): Observable<CurrentUiUser> {
		return this.currentUserSubject;
	}
}
