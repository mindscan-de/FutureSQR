import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';

import { AccountRoutingModule } from './account-routing.module';
import { LoginComponent } from './login/login.component';
import { AccountLayoutComponent } from './account-layout/account-layout.component';
import { ReauthWsComponent } from './reauth-ws/reauth-ws.component';
import { RedirectComponent } from './redirect/redirect.component';


@NgModule({
  declarations: [
	LoginComponent, 
	AccountLayoutComponent, ReauthWsComponent, RedirectComponent],
  imports: [
    CommonModule,
    AccountRoutingModule,
	FormsModule,
	ReactiveFormsModule
  ]
})
export class AccountModule { }
