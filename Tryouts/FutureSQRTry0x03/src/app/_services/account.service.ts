import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

import { User } from '../_models/user';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
	private userSubject: BehaviorSubject<User>;
	public user: Observable<User>;

	constructor() {
		this.userSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('user')));
		this.user = this.userSubject.asObservable();
	}
	
	public get userValue(): User {
		return this.userSubject.value;
	}
	
	// TODO: login
	// TODO: logout
	// TODO: register
	// TODO: getAll
	// TODO: getById
	// TODO: update
	
}
