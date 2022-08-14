import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import {HomeComponent } from './views/home/home.component';
import {UsersComponent } from './views/users/users.component';

const routes: Routes = [
	{ path: '', component: HomeComponent },
	{ path: 'users', component: UsersComponent},
	
	// { path: 'account'}
	
	// otherwise redirect to home
	{ path: '**', redirectTo:''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
