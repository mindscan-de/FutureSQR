import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AccountLayoutComponent } from './account-layout/account-layout.component';
import { LoginComponent } from '../views/login/login.component';

const routes: Routes = [
	{
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
