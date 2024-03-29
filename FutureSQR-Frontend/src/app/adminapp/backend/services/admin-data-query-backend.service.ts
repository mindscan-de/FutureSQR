import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map,first } from 'rxjs/operators';


import { AdminBackendModelSimpleUserItem } from '../model/admin-backend-model-simple-user-item';
import { AdminBackendScmProjectConfiguration } from '../model/admin-backend-scm-project-configuration';

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
		
		return this.httpClient.post<AdminBackendModelSimpleUserItem>( restURL, formdata).pipe( first() );
	}

	public postUnbanUser(userUuid:string): Observable<AdminBackendModelSimpleUserItem> {
		let restURL = '/FutureSQR/rest/user/unban';
		
		let formdata = new FormData();
		
		formdata.append('userUuid',userUuid);
		
		return this.httpClient.post<AdminBackendModelSimpleUserItem>( restURL, formdata).pipe( first() );
	}
	
	public postAddUser(username:string, displayname: string, emailcontact: string, password: string): Observable<AdminBackendModelSimpleUserItem> {
		let restURL = '/FutureSQR/rest/user/add';
		
		let formdata = new FormData();
		formdata.append('userName', username);
		formdata.append('displayName', displayname);
		formdata.append('contactEmail', emailcontact);
		formdata.append('password', password);
		
		return this.httpClient.post<AdminBackendModelSimpleUserItem>( restURL, formdata).pipe(first());
	}
	
	public postAddProject( repositoryURL: string, displayname: string, projectid: string, reviewprefix: string, projectdescription: string):Observable<AdminBackendScmProjectConfiguration> {
		let restURL = '/FutureSQR/rest/admin/project/add';
		
		let formdata = new FormData();
		formdata.append('scmRepositoryURL', repositoryURL);
		formdata.append('scmProjectDisplayName', displayname);
		formdata.append('scmProjectId', projectid);
		formdata.append('scmProjectReviewPrefix',reviewprefix);
		formdata.append('scmProjectDescription', projectdescription);
		
		return this.httpClient.post<AdminBackendScmProjectConfiguration>(restURL, formdata).pipe(first());
	}
	
	public getProjectConfiguration( projectid:string ):Observable<AdminBackendScmProjectConfiguration> {
		let restURL = `/FutureSQR/rest/admin/project/${projectid}/configuration`;
		
		return this.httpClient.get<AdminBackendScmProjectConfiguration>(restURL).pipe(first());
	}
	
	public postReinitDatabase() : Observable<any> {
		let restURL = '/FutureSQR/rest/admin/database/reinitDatabase';
		
		let formdata = new FormData();
		
		return this.httpClient.post<any>(restURL, formdata).pipe(first());
	}
	
	public postBackupDatabase() : Observable<any> {
		let restURL = '/FutureSQR/rest/admin/database/backupDatabase';
		
		let formdata = new FormData();
		
		return this.httpClient.post<any>(restURL, formdata).pipe(first());
	}
	
	public postRestoreDatabase( backupkey:string ) : Observable<any> {
		let restURL = '/FutureSQR/rest/admin/database/restoreDatabase';
		
		let formdata = new FormData();
		formdata.append('backupKey', backupkey);
		
		return this.httpClient.post<any>(restURL, formdata).pipe(first());
	}
	
}
