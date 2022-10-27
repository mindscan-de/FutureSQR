import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

import { CurrentAuthorizations } from './model/current-authorizations';

@Injectable({
  providedIn: 'root'
})
export class AuthZService {
	
	private _currentAuthorizationsValue:CurrentAuthorizations = new CurrentAuthorizations();
	
	private _currentAuthorizationsSubject: BehaviorSubject<CurrentAuthorizations>;
	private currentAuthorizationsSubject: Observable<CurrentAuthorizations>;

	constructor() { 
		this._currentAuthorizationsSubject = new BehaviorSubject<CurrentAuthorizations>(this._currentAuthorizationsValue);
		this.currentAuthorizationsSubject = this._currentAuthorizationsSubject.asObservable();
	}
	
	asObservable(): Observable<CurrentAuthorizations> {
		return this.currentAuthorizationsSubject;
	}
}
