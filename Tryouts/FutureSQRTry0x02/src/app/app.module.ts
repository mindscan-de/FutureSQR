import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CodemirrorModule } from '@ctrl/ngx-codemirror'

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DualDiffComponent } from './ui/dual-diff/dual-diff.component';
import { SingleDiffComponent } from './ui/single-diff/single-diff.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MySingleDiffViewComponent } from './views/my-single-diff-view/my-single-diff-view.component';
import { MyDualDiffViewComponent } from './views/my-dual-diff-view/my-dual-diff-view.component';

@NgModule({
  declarations: [
    AppComponent,
    DualDiffComponent,
    SingleDiffComponent,
    MySingleDiffViewComponent,
    MyDualDiffViewComponent
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
