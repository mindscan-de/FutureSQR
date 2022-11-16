import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { AdminBackendModelSimpleUserItem } from '../../backend/model/admin-backend-model-simple-user-item';
import { AdminDataQueryBackendService } from '../../backend/services/admin-data-query-backend.service';

// Admin App Services
import { AdminNavigationBarService } from '../../services/admin-navigation-bar.service';
import { AdminNavbarBreadcrumbItem } from '../../services/model/admin-navbar-breadcrumb-item';


@Component({
	selector: 'app-configure-user',
	templateUrl: './configure-user.component.html',
	styleUrls: ['./configure-user.component.css']
})
export class ConfigureUserComponent implements OnInit {

	public activeUserID: string = '';
	public activeUser?: AdminBackendModelSimpleUserItem;

	constructor(
		private adminNavigationBarService : AdminNavigationBarService,
		private adminDataQueryBackend : AdminDataQueryBackendService,
		private route : ActivatedRoute,
	) { }

	ngOnInit(): void {
		this.activeUserID = this.route.snapshot.paramMap.get('useruuid');

		this.updateNavigationBar(this.activeUserID);

		this.adminDataQueryBackend.getAdminUserList().subscribe(
			data => { this.onAccountListProvided(data) },
			error => { }
		);
	}

	private onAccountListProvided(userlist: AdminBackendModelSimpleUserItem[]): void {
		let user = userlist.find(user => user.uuid === this.activeUserID)
		
		if (user) {
			this.activeUser = this.m2mTransform(user);
			this.updateNavigationBar(this.activeUser.displayname)
		}
	}

	private m2mTransform(input: AdminBackendModelSimpleUserItem): AdminBackendModelSimpleUserItem {
		return input;
	}

	private updateNavigationBar(username: string): void {
		let x: AdminNavbarBreadcrumbItem[] = []

		x.push(new AdminNavbarBreadcrumbItem('users', ['users'], false));
		x.push(new AdminNavbarBreadcrumbItem(username, ['user', this.activeUserID], true));

		this.adminNavigationBarService.setAdminBreadCrumbNavigation(x);
	}

	onBanUser() {
		let that = this;
		this.adminDataQueryBackend.postBanUser(this.activeUser.uuid).subscribe(
			data => { this.activeUser = data; }
		)
	}

	onUnbanUser() {
		let that = this;
		this.adminDataQueryBackend.postUnbanUser(this.activeUser.uuid).subscribe(
			data => { this.activeUser = data; }
		)
	}
}
