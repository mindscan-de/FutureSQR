import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AccountLayoutComponent } from './account-layout/account-layout.component';
import { LoginComponent } from '../views/login/login.component';

const routes: Routes = [
	{
		// we have a Account Layout Component, having it's  own router outlet.
		path: '', component: AccountLayoutComponent,
		children: [
			{ path: 'login', component: LoginComponent }
			
		]
		
	}
	
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AccountRoutingModule { }
