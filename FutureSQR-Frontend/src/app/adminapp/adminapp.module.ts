import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminappRoutingModule } from './adminapp-routing.module';
import { AdminappComponent } from './adminapp.component';
import { ConfigureProjectComponent } from './views/configure-project/configure-project.component';
import { ConfigureProjectsComponent } from './views/configure-projects/configure-projects.component';


@NgModule({
  declarations: [
	AdminappComponent, 
	ConfigureProjectComponent, 
	ConfigureProjectsComponent
	],
  imports: [
    CommonModule,
    AdminappRoutingModule
  ]
})
export class AdminappModule { }
