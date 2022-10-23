import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AdminappComponent } from './adminapp.component';
import { ConfigureProjectComponent } from './views/configure-project/configure-project.component';
import { ConfigureProjectsComponent } from './views/configure-projects/configure-projects.component';
import { ConfigureUsersComponent } from './views/configure-users/configure-users.component';
import { ConfigureGroupsComponent } from './views/configure-groups/configure-groups.component';

const routes: Routes = [
	// show the main admin page
	{ path: '', component: AdminappComponent },
	
	// projects
	{ path: 'project/:projectid', component: ConfigureProjectComponent },
	{ path: 'projects' , component: ConfigureProjectsComponent },
	
	// users
	// TODO: user/:useruuid
	{ path: 'users' , component: ConfigureUsersComponent },
	
	// groups
	// TODO: groups/:groupuuid
	{ path: 'groups' , component: ConfigureGroupsComponent },
	
	// catch all other unknown URLs
	{ path:'**',redirectTo:''}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminappRoutingModule { }
