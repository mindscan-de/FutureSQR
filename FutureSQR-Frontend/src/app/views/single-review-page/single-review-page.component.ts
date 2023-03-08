import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Backend Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// Intermal Services
import { NavigationBarService } from '../../uiservices/navigation-bar.service';

// M2M Transformation
import { TransformCommitRevision } from '../../m2m/transform-commit-revision';
import { TransformChangeSet } from '../../m2m/transform-change-set';

// Backend Model
import { BackendModelReviewData } from '../../backend/model/backend-model-review-data';
import { BackendModelSingleCommitFullChangeSet } from '../../backend/model/backend-model-single-commit-full-change-set';
import { BackendModelProjectRecentCommitRevision } from '../../backend/model/backend-model-project-recent-commit-revision';

// UI Model
import { UiReviewFileInformation } from '../../commonui/uimodel/ui-review-file-information';
import { UiFileChangeSetModel } from '../../commonui/uimodel/ui-file-change-set-model';

// DIRTY HACK - make sure the BPE encode is initialized early....
import { BpeEncoderProviderService } from '../../incubator/bpe/bpe-encoder-provider.service';

@Component({
  selector: 'app-single-review-page',
  templateUrl: './single-review-page.component.html',
  styleUrls: ['./single-review-page.component.css']
})
export class SingleReviewPageComponent implements OnInit {
	
	public activeProjectID: string = '';
	public activeReviewID: string = '';
	public uiFileInformations: UiReviewFileInformation[] = [];  

    public uiModelSingleRevisionDiffs: BackendModelSingleCommitFullChangeSet = new BackendModelSingleCommitFullChangeSet();
	public uiActiveChangeSetReviewDiff: UiFileChangeSetModel[] = [];
		
	public uiRevisionInformation: BackendModelProjectRecentCommitRevision[] = [];
	
	public activeReviewData: BackendModelReviewData = new BackendModelReviewData();

	constructor( 
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private navigationBarService : NavigationBarService,
		// actually this is not nice here, anyhow.... DIRTY HACK.
		private encoder: BpeEncoderProviderService,
		private route: ActivatedRoute 
	) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		this.activeReviewID = this.route.snapshot.paramMap.get('reviewid');
		
		this.projectDataQueryBackend.getReviewData(this.activeProjectID, this.activeReviewID).subscribe(
			data => this.onReviewDataReceived(data),
			error => {}			
		);
		
		// query the filelist for this review
		this.projectDataQueryBackend.getReviewFilePathsData(this.activeProjectID,this.activeReviewID ).subscribe (
			data => this.onFileListActionsProvided(TransformCommitRevision.convertToUiReviewFileinformationArray(data)),
			error => console.log(error)
		);

		// query some revision information for this particular review
		this.projectDataQueryBackend.getReviewSimpleRevisionInformationList(this.activeProjectID,this.activeReviewID).subscribe(
			data => this.onReviewRevisionInformation(data),
			error => {}
		);
	}
	
	onReviewDataReceived(reviewData: BackendModelReviewData):  void {
		this.activeReviewData = reviewData;
		this.setNavigationInformation();
	}
	
	setNavigationInformation(): void {
		let x = []

		x.push( this.navigationBarService.createItem( this.activeProjectID, ['/',this.activeProjectID], false ));
		x.push( this.navigationBarService.createItem( 'Reviews', ['/',this.activeProjectID,'reviews'], false ));
		x.push( this.navigationBarService.createItem( this.activeReviewID+": "+this.activeReviewData.reviewTitle, ['/',this.activeProjectID, 'review', this.activeReviewID], true ));
		
		this.navigationBarService.setBreadcrumbNavigation(x);
	}
	
	onDiffDataReceived( diffData: BackendModelSingleCommitFullChangeSet ):void {
		this.uiModelSingleRevisionDiffs = diffData;
		
		this.uiActiveChangeSetReviewDiff=diffData.fileChangeSet.map(
				(item)=> TransformChangeSet.fromBackendFileChangeSetToUiFileChangeSet(item)
			);
		
	}

	onFileListActionsProvided( fileInformations : UiReviewFileInformation[]) : void {
		this.uiFileInformations = fileInformations;
	}
	
	onRevisionActivationChanged(activations:string):void {
		console.log("Guess what");
		console.log(activations);
		
		// TODO: reload unified diff data from server, depending on the current revision selection.
		// envdata should be a list of revisions enabled.
		// the single page has a list of revisions
		
		// we must encode the list as a single parameter
		// we must manipulate the parame parameter map, so in the link can be given a configuration
		
		this.projectDataQueryBackend.getReviewRevisionDiffFullChangeSet(this.activeProjectID,this.activeReviewID, activations ).subscribe(
			data => this.onDiffDataReceived(data),
			error => {}
		);
		
	}
	
	onProjectInformationProvided(projectinformation:any):void {
		
	}
	
	onReviewRevisionInformation(data): void {
		this.uiRevisionInformation = data;
	}
	
	reloadReviewInformation(event):void {
		this.projectDataQueryBackend.getReviewData(this.activeProjectID, this.activeReviewID).subscribe(
			data => this.onReviewDataReceived(data),
			error => {}			
		);
	}
	
	reloadRevisionInformation(event):void {
		// reload revision list data
		this.projectDataQueryBackend.getReviewSimpleRevisionInformationList(this.activeProjectID,this.activeReviewID).subscribe(
			data => this.onReviewRevisionInformation(data),
			error => {}
		);

		// we have to query a new file list, when the revisions were changed		
		this.projectDataQueryBackend.getReviewFilePathsData(this.activeProjectID,this.activeReviewID ).subscribe (
			data => this.onFileListActionsProvided(TransformCommitRevision.convertToUiReviewFileinformationArray(data)),
			error => console.log(error)
		);
		
		// TODO: also we should very likely review the file changesets, but only after completion of revision management dialogs.

		// HACK... / we actually only want to reload the review authors.
		this.reloadReviewInformation(event);
	}
	
}
