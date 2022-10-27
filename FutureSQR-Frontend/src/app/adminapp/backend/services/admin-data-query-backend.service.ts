import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map,first } from 'rxjs/operators';


import { AdminBackendModelSimpleUserItem } from '../model/admin-backend-model-simple-user-item';

@Injectable({
  providedIn: 'root'
})
export class AdminDataQueryBackendService {

	constructor(
		private httpClient: HttpClient
	) { }
	
	
	
	public getAdminUserList(): Observable<AdminBackendModelSimpleUserItem[]> {
		let restURL = '/FutureSQR/rest/user/adminuserlist';
		
		// TODO: actually this needs to be checked for rights, to get a complete userlist
		return this.httpClient.get<AdminBackendModelSimpleUserItem[]>( restURL ).pipe( first() );
	}
	
	public postBanUser(userUuid:string): Observable<AdminBackendModelSimpleUserItem> {
		let restURL = '/FutureSQR/rest/user/ban';
		
		let formdata = new FormData();
		
		formdata.append('userUuid',userUuid);
		
		// make sure we automatically unsubscribe / otherwise memory leak
		return this.httpClient.post<AdminBackendModelSimpleUserItem>( restURL, formdata).pipe( first() );
	}

	public postUnbanUser(userUuid:string): Observable<AdminBackendModelSimpleUserItem> {
		let restURL = '/FutureSQR/rest/user/unban';
		
		let formdata = new FormData();
		
		formdata.append('userUuid',userUuid);
		
		// make sure we automatically unsubscribe / otherwise memory leak
		return this.httpClient.post<AdminBackendModelSimpleUserItem>( restURL, formdata).pipe( first() );
	}
	
	
}
