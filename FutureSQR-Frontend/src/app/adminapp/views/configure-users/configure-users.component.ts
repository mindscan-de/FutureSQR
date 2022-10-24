import { Component, OnInit } from '@angular/core';

// Admin App Services
import { AdminNavigationBarService }  from '../../services/admin-navigation-bar.service';
import { AdminNavbarBreadcrumbItem } from '../../services/model/admin-navbar-breadcrumb-item';

import { AdminDataQueryBackendService } from '../../backend/services/admin-data-query-backend.service';
import { AdminBackendModelSimpleUserItem } from '../../backend/model/admin-backend-model-simple-user-item';


@Component({
  selector: 'app-configure-users',
  templateUrl: './configure-users.component.html',
  styleUrls: ['./configure-users.component.css']
})
export class ConfigureUsersComponent implements OnInit {

	public uiModelSimpleUserlist: AdminBackendModelSimpleUserItem[] = [];

	constructor(
 		private adminNavigationBarService : AdminNavigationBarService,
		private adminDataQueryBackend : AdminDataQueryBackendService,
	) { }

	ngOnInit(): void {
		this.updateNavigationBar();
		
		this.adminDataQueryBackend.getAdminUserList().subscribe(
			data => {this.onAccountListProvided(data)},
			error => {}
		);
	}
	
	private onAccountListProvided( userlist: AdminBackendModelSimpleUserItem[]): void {
		this.uiModelSimpleUserlist = this.m2mTransform(userlist);
	}
	
	private m2mTransform(input: AdminBackendModelSimpleUserItem[]): AdminBackendModelSimpleUserItem[] {
		return input;
	}
	
	
	updateNavigationBar() : void {
		let x:AdminNavbarBreadcrumbItem[] = []
		
		x.push(new AdminNavbarBreadcrumbItem('users', ['users'], true));
		 
		this.adminNavigationBarService.setAdminBreadCrumbNavigation(x);
	}
	
	onBanUser(user: AdminBackendModelSimpleUserItem) : void {
/*		let that = this;
		this.accoutService.postBanUser(user.loginname).subscribe(
			data => { 
				// result entry after baning, refresh user list after baning 
				that.updateUserList(data);  
				},
			error => {}
		)
*/	}

	onUnbanUser(user:AdminBackendModelSimpleUserItem) : void {
/*		let that = this;
		this.accoutService.postUnbanUser(user.loginname).subscribe(
			data => { 
				// result entry after baning, refresh user list after baning  
				that.updateUserList(data);
				},
			error => {}
		)
*/		
	}

	
	updateUserList(user: AdminBackendModelSimpleUserItem):void {
/*		// currently we simply reload the data, actually we should patch it.
		this.accoutService.getAdminUserList().subscribe(
			data => {this.onAccountListProvided(data)},
			error => {}
		);
*/	}
	

}
