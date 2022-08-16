import { Component, OnInit } from '@angular/core';

import { AccountService } from '../../_services/account.service';

@Component({
  selector: 'app-account-layout',
  templateUrl: './account-layout.component.html',
  styleUrls: ['./account-layout.component.css']
})
export class AccountLayoutComponent implements OnInit {

	constructor(
		private accountService: AccountService
	) { }

  ngOnInit(): void {
  }

}
