import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { map,first } from 'rxjs/operators';


import { User } from '../_models/user';
import { BackendModelSimpleUserItem } from '../_models/backend-model-simple-user-item';



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
				console.log("User was found...")
				console.log(user);
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
	
	public getAdminUserList(): Observable<BackendModelSimpleUserItem[]> {
		let restURL = '/FutureSQR/rest/user/adminuserlist';
		
		// TODO: actually this needs to be checked for rights, to get a complete userlist
		return this.httpClient.get<BackendModelSimpleUserItem[]>( restURL ).pipe( first() );
	}
	
	public postBanUser(username:string): Observable<BackendModelSimpleUserItem> {
		let restURL = '/FutureSQR/rest/user/ban';
		
		let formdata = new FormData();
		
		formdata.append('username',username);
		
		// make sure we automatically unsubscribe / otherwise memory leak
		return this.httpClient.post<BackendModelSimpleUserItem>( restURL, formdata).pipe( first() );
	}

	public postUnbanUser(username:string): Observable<BackendModelSimpleUserItem> {
		let restURL = '/FutureSQR/rest/user/unban';
		
		let formdata = new FormData();
		
		formdata.append('username',username);
		
		// make sure we automatically unsubscribe / otherwise memory leak
		return this.httpClient.post<BackendModelSimpleUserItem>( restURL, formdata).pipe( first() );
	}

	public postAddUser(username:string, displayname, emailcontact, password): Observable<BackendModelSimpleUserItem> {
		let restURL = '/FutureSQR/rest/user/add';
		
		let formdata = new FormData();
		formdata.append('username', username);
		formdata.append('displayname', displayname);
		formdata.append('contactemail', emailcontact);
		formdata.append('password', password);
		
		// make sure we automatically unsubscribe
		return this.httpClient.post<BackendModelSimpleUserItem>( restURL, formdata).pipe(first());
	}
	
	// TODO: getById
	// TODO: update
	
}
