import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainComponent } from './main/main.component';
import { ReviewComponent } from './review/review.component';
import { RevisionComponent } from './revision/revision.component';

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    ReviewComponent,
    RevisionComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
