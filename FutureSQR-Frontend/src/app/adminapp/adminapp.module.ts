import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminappRoutingModule } from './adminapp-routing.module';
import { AdminappComponent } from './adminapp.component';
import { ConfigureProjectComponent } from './views/configure-project/configure-project.component';
import { ConfigureProjectsComponent } from './views/configure-projects/configure-projects.component';
import { ConfigureGroupsComponent } from './views/configure-groups/configure-groups.component';
import { ConfigureGroupComponent } from './views/configure-group/configure-group.component';
import { ConfigureUsersComponent } from './views/configure-users/configure-users.component';
import { ConfigureUserComponent } from './views/configure-user/configure-user.component';
import { ConfigureAddUserComponent } from './views/configure-add-user/configure-add-user.component';


@NgModule({
  declarations: [
	AdminappComponent, 
	ConfigureProjectComponent, 
	ConfigureProjectsComponent, ConfigureGroupsComponent, ConfigureGroupComponent, ConfigureUsersComponent, ConfigureUserComponent, ConfigureAddUserComponent
	],
  imports: [
    CommonModule,
    AdminappRoutingModule
  ]
})
export class AdminappModule { }
