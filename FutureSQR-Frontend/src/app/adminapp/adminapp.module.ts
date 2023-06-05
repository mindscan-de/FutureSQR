import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReactiveFormsModule } from '@angular/forms';

import { AdminappRoutingModule } from './adminapp-routing.module';
import { AdminappComponent } from './adminapp.component';
import { ConfigureProjectComponent } from './views/configure-project/configure-project.component';
import { ConfigureProjectsComponent } from './views/configure-projects/configure-projects.component';
import { ConfigureGroupsComponent } from './views/configure-groups/configure-groups.component';
import { ConfigureGroupComponent } from './views/configure-group/configure-group.component';
import { ConfigureUsersComponent } from './views/configure-users/configure-users.component';
import { ConfigureUserComponent } from './views/configure-user/configure-user.component';
import { ConfigureAddUserComponent } from './views/configure-add-user/configure-add-user.component';
import { ConfigureAddGroupComponent } from './views/configure-add-group/configure-add-group.component';
import { ConfigureAddProjectComponent } from './views/configure-add-project/configure-add-project.component';
import { AdminappAvatarOnlyComponent } from './commonui/adminapp-avatar-only/adminapp-avatar-only.component';
import { ConfigureSystemDatabaseComponent } from './views/configure-system-database/configure-system-database.component';
import { ConfigureSystemComponent } from './views/configure-system/configure-system.component';

@NgModule({
  declarations: [
	AdminappComponent, 
	ConfigureProjectComponent, 
	ConfigureProjectsComponent, 
	ConfigureGroupsComponent, 
	ConfigureGroupComponent, 
	ConfigureUsersComponent, 
	ConfigureUserComponent, 
	ConfigureAddUserComponent, 
	ConfigureAddGroupComponent, 
	ConfigureAddProjectComponent, 
	AdminappAvatarOnlyComponent, 
	ConfigureSystemDatabaseComponent, ConfigureSystemComponent
	],
  imports: [
    CommonModule,
	ReactiveFormsModule,
    AdminappRoutingModule
  ]
})
export class AdminappModule { }
