import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

// 3rd party components
import { CodemirrorModule } from '@ctrl/ngx-codemirror'

// Routing and Components
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// Pages
import { MainPageComponent } from './views/main-page/main-page.component';
import { StarredProjectsComponent } from './views/main-page/starred-projects/starred-projects.component';
import { MostActiveProjectsComponent } from './views/main-page/most-active-projects/most-active-projects.component';
import { AllProjectsPageComponent } from './views/all-projects-page/all-projects-page.component';
import { ProjectRecentCommitsPageComponent } from './views/project-recent-commits-page/project-recent-commits-page.component';
import { BasicProjectInformationComponent } from './views/project-recent-commits-page/basic-project-information/basic-project-information.component';
import { SingleRevisionPageComponent } from './views/single-revision-page/single-revision-page.component';
import { SingleReviewPageComponent } from './views/single-review-page/single-review-page.component';
import { ContentChangeSetSingleDiffComponent } from './commonui/content-change-set-single-diff/content-change-set-single-diff.component';
import { FileChangeSetSingleDiffComponent } from './commonui/file-change-set-single-diff/file-change-set-single-diff.component';
import { ContentChangeSetSideBySideDiffComponent } from './commonui/content-change-set-side-by-side-diff/content-change-set-side-by-side-diff.component';
import { FileChangeSetSideBySideDiffComponent } from './commonui/file-change-set-side-by-side-diff/file-change-set-side-by-side-diff.component';
import { SingleRevisionSideBySideDialogComponent } from './commonui/single-revision-side-by-side-dialog/single-revision-side-by-side-dialog.component';
import { ReviewParticipationPanelComponent } from './views/single-review-page/review-participation-panel/review-participation-panel.component';
import { ProjectRecentReviewsPageComponent } from './views/project-recent-reviews-page/project-recent-reviews-page.component';
import { RevisionParticipationPanelComponent } from './views/single-review-page/revision-participation-panel/revision-participation-panel.component';
import { AddRevisionToReviewSelectionDialogComponent } from './commonui/add-revision-to-review-selection-dialog/add-revision-to-review-selection-dialog.component';
import { BasicReviewInformationComponent } from './views/single-review-page/basic-review-information/basic-review-information.component';
import { RevisionSelectionPanelComponent } from './views/single-review-page/revision-selection-panel/revision-selection-panel.component';
import { BasicRevisionInformationComponent } from './views/single-revision-page/basic-revision-information/basic-revision-information.component';
import { RevisionActionPanelComponent } from './views/single-revision-page/revision-action-panel/revision-action-panel.component';
import { ReviewDiscussionPanelComponent } from './views/single-review-page/review-discussion-panel/review-discussion-panel.component';
import { FileParticipationFileListPanelComponent } from './commonui/file-participation-file-list-panel/file-participation-file-list-panel.component';
import { SingleReviewThreadPanelComponent } from './commonui/single-review-thread-panel/single-review-thread-panel.component';

@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    StarredProjectsComponent,
    MostActiveProjectsComponent,
    AllProjectsPageComponent,
    ProjectRecentCommitsPageComponent,
    BasicProjectInformationComponent,
    SingleRevisionPageComponent,
    SingleReviewPageComponent,
    ContentChangeSetSingleDiffComponent,
    FileChangeSetSingleDiffComponent,
    ContentChangeSetSideBySideDiffComponent,
    FileChangeSetSideBySideDiffComponent,
    SingleRevisionSideBySideDialogComponent,
    ReviewParticipationPanelComponent,
    ProjectRecentReviewsPageComponent,
    RevisionParticipationPanelComponent,
    AddRevisionToReviewSelectionDialogComponent,
    BasicReviewInformationComponent,
    RevisionSelectionPanelComponent,
    BasicRevisionInformationComponent,
    RevisionActionPanelComponent,
    ReviewDiscussionPanelComponent,
    FileParticipationFileListPanelComponent,
    SingleReviewThreadPanelComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
	FormsModule,
	CodemirrorModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
