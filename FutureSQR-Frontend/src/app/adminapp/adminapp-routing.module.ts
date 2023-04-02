import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AdminappComponent } from './adminapp.component';
import { ConfigureProjectComponent } from './views/configure-project/configure-project.component';
import { ConfigureProjectsComponent } from './views/configure-projects/configure-projects.component';
import { ConfigureAddProjectComponent } from './views/configure-add-project/configure-add-project.component';
import { ConfigureUserComponent } from './views/configure-user/configure-user.component';
import { ConfigureAddUserComponent } from './views/configure-add-user/configure-add-user.component';
import { ConfigureUsersComponent } from './views/configure-users/configure-users.component';
import { ConfigureGroupComponent } from './views/configure-group/configure-group.component';
import { ConfigureGroupsComponent } from './views/configure-groups/configure-groups.component';


const routes: Routes = [
	// show the main admin page
	{ path: '', component: AdminappComponent },
	
	// projects
	{ path: 'project/:projectid', component: ConfigureProjectComponent },
	{ path: 'projects/add', component: ConfigureAddProjectComponent },
	{ path: 'projects' , component: ConfigureProjectsComponent },
	
	// users
	{ path: 'user/:useruuid' , component: ConfigureUserComponent },
	{ path: 'users/add' , component: ConfigureAddUserComponent },
	{ path: 'users' , component: ConfigureUsersComponent },
	
	// groups
	{ path: 'group/:groupuuid' , component: ConfigureGroupComponent },
	{ path: 'groups' , component: ConfigureGroupsComponent },
	
	// TODO: servers - this is when we start communicating with servers directly, instead of local checked out repositories
	
	// catch all other unknown URLs
	{ path:'**',redirectTo:''}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminappRoutingModule { }
