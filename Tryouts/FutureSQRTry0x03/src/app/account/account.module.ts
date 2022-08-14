import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AccountRoutingModule } from './account-routing.module';

import { LoginComponent } from '../views/login/login.component';


@NgModule({
  imports: [
    CommonModule,
    AccountRoutingModule
  ],
  declarations: [
	LoginComponent
  ]
})
export class AccountModule { }
