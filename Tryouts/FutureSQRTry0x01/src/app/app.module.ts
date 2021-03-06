import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainComponent } from './main/main.component';
import { ReviewComponent } from './review/review.component';
import { RevisionComponent } from './revision/revision.component';
import { DiffComponent } from './diff/diff.component';
import { ProjectComponent } from './project/project.component';
import { StarredProjectsComponent } from './main/starred-projects/starred-projects.component';
import { AllProjectsComponent } from './all-projects/all-projects.component';

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    ReviewComponent,
    RevisionComponent,
    DiffComponent,
    ProjectComponent,
    StarredProjectsComponent,
    AllProjectsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
