import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
	
	// { path:'', pathMatch: 'full', component: MainComponent},
	// this path should lead to a component, where we will show some lists.
	// { path:'p/{projectname}'}
	// this path should lead to a project containing all recent commits as a list
	// { path:'p/{projectname}/revision/{revisonid}}'}
	// this path should lead to a certain revision
	// { path:'p/{projectname}/diff/{revisionid}'}
	// this path should lead to a diff to the previous version
	// { path:'login', loadChildren: './authentication/authentication.module#AuthenticationModule' }
	// 
	
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
