import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
	
	// { path:'', pathMatch: 'full', component: MainComponent},
	// this path should lead to a component, where we will show some project lists customized tot the user

	// { path:'p/{projectname}/review/{reviewname}'}
	// this path should lead to a review in the project.
	
	// { path:'p/{projectname}/revision/{revisonid}}'}
	// this path should lead to a certain revision and a list of diffs for this particular version in the project
	
	// { path:'p/{projectname}/diff/{revisionid}'}
	// this path should lead to a diff to the previous version
	
	// { path:'p/{projectname}'}
	// this path should lead to a project containing all recent commits as a list
	
	// { path:'login/', loadChildren: './authentication/authentication.module#AuthenticationModule' }
	// 
	
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
