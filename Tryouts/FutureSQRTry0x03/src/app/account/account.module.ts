import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { AccountRoutingModule } from './account-routing.module';

import { LoginComponent } from '../views/login/login.component';
import { AccountLayoutComponent } from './account-layout/account-layout.component';


@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AccountRoutingModule
  ],
  declarations: [
	LoginComponent,
	AccountLayoutComponent
  ]
})
export class AccountModule { }
