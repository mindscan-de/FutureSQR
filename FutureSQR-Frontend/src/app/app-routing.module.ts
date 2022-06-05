import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Views
import { MainPageComponent } from './views/main-page/main-page.component';

const routes: Routes = [
	{ path:'', pathMatch: 'full', component: MainPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
