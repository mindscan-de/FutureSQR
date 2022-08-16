import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './views/home/home.component';
import { UsersComponent } from './views/users/users.component';
import { AuthGuardService } from './_helpers/auth-guard.service';


const accountModule = () => import('./account/account.module').then(x => x.AccountModule);

const routes: Routes = [
	{ path: '', component: HomeComponent, canActivate: [AuthGuardService] },
	{ path: 'users', component: UsersComponent , canActivate: [AuthGuardService] },
	
	// login, register
	{ path: 'account', loadChildren: accountModule },
	
	// otherwise redirect to home
	{ path: '**', redirectTo:''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
