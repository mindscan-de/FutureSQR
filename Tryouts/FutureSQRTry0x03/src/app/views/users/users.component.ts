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
	}
	
	onBanUser(user: BackendModelSimpleUserItem) : void {
		this.accoutService.postBanUser(user.loginname).subscribe(
			data => { /* result entry after baning, refresh user list after baning */  },
			error => {}
		)
	}
}
