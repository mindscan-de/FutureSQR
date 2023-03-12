import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AuthNGuardService } from './authn/auth-n-guard.service';

// Views
import { MainPageComponent } from './views/main-page/main-page.component';
import { AllProjectsPageComponent } from './views/all-projects-page/all-projects-page.component';
import { ProjectRecentCommitsPageComponent } from './views/project-recent-commits-page/project-recent-commits-page.component';
import { ProjectRecentReviewsPageComponent }  from './views/project-recent-reviews-page/project-recent-reviews-page.component';
import { SingleRevisionPageComponent } from './views/single-revision-page/single-revision-page.component';
import { SingleReviewPageComponent } from './views/single-review-page/single-review-page.component';
import { SingleFileRevisionPageComponent } from './views/single-file-revision-page/single-file-revision-page.component';
import { ProjectBranchesPageComponent } from './views/project-branches-page/project-branches-page.component';
import { ProjectBranchRecentCommitsPageComponent } from './views/project-branch-recent-commits-page/project-branch-recent-commits-page.component';

const routes: Routes = [
	// show the main Page
	{ path:'', pathMatch: 'full', component: MainPageComponent, canActivate: [AuthNGuardService] },
	
	// Account pages (login/logout)
	{ path: 'account', loadChildren: () => import('./account/account.module').then(x => x.AccountModule) },

	// start the admin app.
	{ path: 'admin', loadChildren: () => import('./adminapp/adminapp.module').then(m => m.AdminappModule), canActivate: [AuthNGuardService] },
	
	// show a list of all available projects for a logged on user
	{ path:'allprojects', pathMatch: 'full', component: AllProjectsPageComponent, canActivate: [AuthNGuardService] },	

	// show a list of all recent project reviews (open and maybe some recent closed)
	{ path:':projectid/reviews', component:ProjectRecentReviewsPageComponent, canActivate: [AuthNGuardService] },
	
	// show a list of branches known for a selecte project - where you can change default branch etc.
	{ path:':projectid/branches', component:ProjectBranchesPageComponent, canActivate: [AuthNGuardService] },
	
	// show the commits for the selected branch of the selected project 
	{ path:':projectid/branch/:branchname', component:ProjectBranchRecentCommitsPageComponent, canActivate: [AuthNGuardService] },
	
	// show a review for a certain project
	{ path:':projectid/review/:reviewid', component:SingleReviewPageComponent, canActivate: [AuthNGuardService] },

	// show a particular file revision
	{ path:':projectid/revision/:revisionid/viewfile', component: SingleFileRevisionPageComponent, canActivate: [AuthNGuardService] }, 
	
	// show a list of files which were changed for a single revision, and their diffs
	// the action area should include some actions related to this particular revision
	{ path:':projectid/revision/:revisionid', component: SingleRevisionPageComponent, canActivate: [AuthNGuardService] },
	
	// TODO: let the diffpage be about about the unified diff? e.g. download patch/diff file etc.
	// :projectid/diff/:revisionid

	// show the recent commits for a certain project
	{ path:':projectid', component:ProjectRecentCommitsPageComponent, canActivate: [AuthNGuardService] },
	
	// catch all other unknown URLs
	{ path:'**',redirectTo:''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
