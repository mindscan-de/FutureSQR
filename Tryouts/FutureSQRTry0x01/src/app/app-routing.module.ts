import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from './main/main.component';
import { ReviewComponent } from './review/review.component';
import { RevisionComponent } from './revision/revision.component';
import { DiffComponent } from './diff/diff.component';
import { ProjectComponent } from './project/project.component';

const routes: Routes = [
	
	{ path:'', pathMatch: 'full', component: MainComponent},
	// this path should lead to a component, where we will show some project lists customized tot the user

	{ path:':projectid/review/:reviewid', component:ReviewComponent},
	// this path should lead to a review in the project.
	
	{ path:':projectid/revision/:revisonid', component:RevisionComponent},
	// this path should lead to a certain revision and a list of diffs for this particular version in the project
	
	{ path:':projectid/diff/:revisionid', component: DiffComponent},
	// this path should lead to a diff to the previous version
	
	{ path:':projectid', component:ProjectComponent},
	// this path should lead to a project containing all recent commits as a list
	
	// { path:'login/', loadChildren: './authentication/authentication.module#AuthenticationModule' }
	// 
	
	// let any other url redirect to main page - for good measures
	{ path:'**',redirectTo:''}
	
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
