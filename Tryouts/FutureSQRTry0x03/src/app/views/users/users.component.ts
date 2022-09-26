import { Component, OnInit } from '@angular/core';

import { AccountService } from '../../_services/account.service';
import { BackendModelSimpleUserItem } from '../../_models/backend-model-simple-user-item';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
	
	public uiModelSimpleUserlist: BackendModelSimpleUserItem[] = []; 

  constructor(
	private accoutService:AccountService	
) { }

  ngOnInit(): void {
	this.accoutService.getAllSimpleList().subscribe(
		data => {this.onAccountListProvided(data)},
		error => {}
	);
  }

	private onAccountListProvided( userlist: BackendModelSimpleUserItem[]): void {
		this.uiModelSimpleUserlist = this.m2mTransform(userlist);
	}
	
	private m2mTransform(input: BackendModelSimpleUserItem[]): BackendModelSimpleUserItem[] {
		return input;
	}
	
	onUnbanUser(user:BackendModelSimpleUserItem) : void {
		// TODO: post unban user
		let that = this;
		this.accoutService.postUnbanUser(user.loginname).subscribe(
			data => { 
				/* result entry after baning, refresh user list after baning */  
				that.updateUserList(data);
				},
			error => {}
		)
		
	}
	
	onBanUser(user: BackendModelSimpleUserItem) : void {
		let that = this;
		this.accoutService.postBanUser(user.loginname).subscribe(
			data => { 
				/* result entry after baning, refresh user list after baning */
				that.updateUserList(data);  
				},
			error => {}
		)
	}
	
	updateUserList(user: BackendModelSimpleUserItem):void {
		// currently we simply reload the data, actually we should patch it.
		this.accoutService.getAllSimpleList().subscribe(
			data => {this.onAccountListProvided(data)},
			error => {}
		);
	}
	
	onCreateUserClicked(): void {
		// TODO: create a NBModal Dialog.
		// the dialog should provide the invocation of the backend
		// on success this modal dialog closes itself
		// or the modal dialog closes itself on cancel
		// in case of successful close, we want to trigger a reload of 
		// the userlist or to patch the userlist and point to the entry (maybe we add user first to the list)   
	}
}
