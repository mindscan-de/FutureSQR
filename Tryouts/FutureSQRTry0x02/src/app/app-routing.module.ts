import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Views
import { MyDualDiffViewComponent } from './views/my-dual-diff-view/my-dual-diff-view.component';
import { MySingleDiffViewComponent } from './views/my-single-diff-view/my-single-diff-view.component';

const routes: Routes = [
	{ path:'dual', pathMatch: 'full', component: MyDualDiffViewComponent},
	{ path:'single', pathMatch: 'full', component: MySingleDiffViewComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
