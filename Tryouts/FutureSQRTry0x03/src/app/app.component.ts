import { Component } from '@angular/core';

import { AccountService }  from './_services/account.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
	title = 'FutureSQRTry0x03';
	public user;
	
	constructor ( 
		private accountService: AccountService
	) {	
	};


	public onLogout(): void {
		this.accountService.logout();
	}
}
