import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Views
import { MainPageComponent } from './views/main-page/main-page.component';
import { AllProjectsPageComponent } from './views/all-projects-page/all-projects-page.component';

const routes: Routes = [
	{ path:'', pathMatch: 'full', component: MainPageComponent},
	{ path:'allprojects', pathMatch: 'full', component: AllProjectsPageComponent}	
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
