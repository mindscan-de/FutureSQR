import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CodemirrorModule } from '@ctrl/ngx-codemirror'

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DualDiffComponent } from './ui/dual-diff/dual-diff.component';
import { SingleDiffComponent } from './ui/single-diff/single-diff.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [
    AppComponent,
    DualDiffComponent,
    SingleDiffComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
	FormsModule,
 	CodemirrorModule,
 	NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
