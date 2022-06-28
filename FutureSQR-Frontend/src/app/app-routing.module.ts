import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Views
import { MainPageComponent } from './views/main-page/main-page.component';
import { AllProjectsPageComponent } from './views/all-projects-page/all-projects-page.component';
import { ProjectRecentCommitsPageComponent } from './views/project-recent-commits-page/project-recent-commits-page.component';
import { ProjectRecentReviewsPageComponent }  from './views/project-recent-reviews-page/project-recent-reviews-page.component';
import { SingleRevisionPageComponent } from './views/single-revision-page/single-revision-page.component';
import { SingleReviewPageComponent } from './views/single-review-page/single-review-page.component';

const routes: Routes = [
	// show the main Page
	{ path:'', pathMatch: 'full', component: MainPageComponent },
	
	// show a list of all available projects for a logged on user
	{ path:'allprojects', pathMatch: 'full', component: AllProjectsPageComponent },	

	// show a list of all recent project reviews (open and maybe some recent closed)
	{ path:':projectid/reviews', component:ProjectRecentReviewsPageComponent },
	
	// show a review for a certain project
	{ path:':projectid/review/:reviewid', component:SingleReviewPageComponent },
	
	// show a list of files which were changed for a single revision, and their diffs
	// the action area should include some actions related to this particular revision
	{ path:':projectid/revision/:revisionid', component: SingleRevisionPageComponent },
	
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
