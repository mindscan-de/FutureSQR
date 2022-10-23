import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminappRoutingModule } from './adminapp-routing.module';
import { AdminappComponent } from './adminapp.component';


@NgModule({
  declarations: [AdminappComponent],
  imports: [
    CommonModule,
    AdminappRoutingModule
  ]
})
export class AdminappModule { }
