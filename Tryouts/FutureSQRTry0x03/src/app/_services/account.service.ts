import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { map } from 'rxjs/operators';

import { User } from '../_models/user';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
	private userSubject: BehaviorSubject<User>;
	public user: Observable<User>;

	constructor(
		private router: Router,
		private httpClient: HttpClient
	) {
		this.userSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('user')));
		this.user = this.userSubject.asObservable();
	}
	
	public get userValue(): User {
		return this.userSubject.value;
	}
	
	public login(username, password) {
		let restURL = '/FutureSQR/rest/user/authenticate';
		
		let formdata = new FormData();
		formdata.append('username', username);
		formdata.append('password', password);
		
		return this.httpClient.post<User>(restURL, formdata).pipe(
			map(user => {
				localStorage.setItem('user', JSON.stringify(user));
				this.userSubject.next(user);
				return user;
			})
		);
	}
	
	public logout() {
		localStorage.removeItem('user');
		this.userSubject.next(null);
		this.router.navigate(['/account/login'])
	}
	// TODO: register
	// TODO: getAll
	// TODO: getById
	// TODO: update
	
}
