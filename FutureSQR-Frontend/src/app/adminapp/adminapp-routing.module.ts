import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AdminappComponent } from './adminapp.component';

const routes: Routes = [
	// show the main admin page
	{ path: '', component: AdminappComponent },
	
	// projects
	
	// users
	
	// groups
	
	// catch all other unknown URLs
	{ path:'**',redirectTo:''}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminappRoutingModule { }
