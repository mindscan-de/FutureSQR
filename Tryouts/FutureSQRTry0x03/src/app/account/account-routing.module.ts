import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from '../views/login/login.component';

const routes: Routes = [
	{
		path: '',
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
