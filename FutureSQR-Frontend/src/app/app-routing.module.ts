import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Views
import { MainPageComponent } from './views/main-page/main-page.component';
import { AllProjectsPageComponent } from './views/all-projects-page/all-projects-page.component';
import { ProjectRecentCommitsPageComponent } from './views/project-recent-commits-page/project-recent-commits-page.component';

const routes: Routes = [
	// show the main Page
	{ path:'', pathMatch: 'full', component: MainPageComponent },
	
	// show a list of all available projects for a logged on user
	{ path:'allprojects', pathMatch: 'full', component: AllProjectsPageComponent },	

	// TODO:
	// :projectid/reviews
	
	// TODO:
	// :projectid/review/:reviewid
	
	// TODO:
	// :projectid/revision/:revisionid
	
	// TODO:
	// :projectid/diff/:revisionid

	// show the recent commits for a certain project
	{ path:':projectid', component:ProjectRecentCommitsPageComponent },
	
	// catch all other unknown URLs
	{ path:'**',redirectTo:''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
