import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminappRoutingModule } from './adminapp-routing.module';
import { AdminappComponent } from './adminapp.component';
import { ConfigureProjectComponent } from './views/configure-project/configure-project.component';
import { ConfigureProjectsComponent } from './views/configure-projects/configure-projects.component';
import { ConfigureGroupsComponent } from './views/configure-groups/configure-groups.component';


@NgModule({
  declarations: [
	AdminappComponent, 
	ConfigureProjectComponent, 
	ConfigureProjectsComponent, ConfigureGroupsComponent
	],
  imports: [
    CommonModule,
    AdminappRoutingModule
  ]
})
export class AdminappModule { }
