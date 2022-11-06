import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


import { AccountLayoutComponent } from './account-layout/account-layout.component';
import { LoginComponent } from './login/login.component';
import { RedirectComponent } from './redirect/redirect.component';
import { ReauthWsComponent} from './reauth-ws/reauth-ws.component';

const routes: Routes = [
	{
		// we have a Account Layout Component, having it's  own router outlet.
		path: '', component: AccountLayoutComponent,

		children: [
			{ path: 'login', component: LoginComponent },
			{ path: 'redirect', component: RedirectComponent },
			{ path: 'reauthws', component: ReauthWsComponent}
		]
	}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AccountRoutingModule { }
