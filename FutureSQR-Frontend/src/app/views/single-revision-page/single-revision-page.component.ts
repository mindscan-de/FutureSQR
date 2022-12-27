import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

// backend services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// internal services
import { NavigationBarService } from '../../uiservices/navigation-bar.service';

// M2M Transformation
import { TransformCommitRevision } from '../../m2m/transform-commit-revision';
import { TransformChangeSet } from '../../m2m/transform-change-set';

// BackendModel - should be actually a ui model 
import { BackendModelSingleCommitFullChangeSet } from '../../backend/model/backend-model-single-commit-full-change-set';
import { BackendModelProjectRecentCommitRevision } from '../../backend/model/backend-model-project-recent-commit-revision';

// UI Model
import { UiReviewFileInformation } from '../../commonui/uimodel/ui-review-file-information';
import { UiFileChangeSetModel } from '../../commonui/uimodel/ui-file-change-set-model';

// Dialog 
import { SingleRevisionSideBySideDialogComponent } from '../../commonui/single-revision-side-by-side-dialog/single-revision-side-by-side-dialog.component';

// Incubator
import { ExperimentalSingleRevisionSideBySideDialogComponent } from '../../incubator/experimental-single-revision-side-by-side-dialog/experimental-single-revision-side-by-side-dialog.component';

// DIRTY HACK - make sure the BPE encode is initialized early....
import { BpeEncoderProviderService } from '../../incubator/bpe/bpe-encoder-provider.service';

// TODO: FIXME: BUGGY URL WHY? ( some UI PROBLEM... let's see another day)
// http://localhost:4200/futuresqr/revision/f01dbd17d5e68f47ec42c4bdd2b923bcc13dce7d

@Component({
  selector: 'app-single-revision-page',
  templateUrl: './single-revision-page.component.html',
  styleUrls: ['./single-revision-page.component.css']
})
export class SingleRevisionPageComponent implements OnInit {
	
	public activeProjectID: string = '';
	public activeRevisionID: string = '';
	public uiFileInformations: UiReviewFileInformation[] = [];
    public uiModelSingleRevisionDiffs: BackendModelSingleCommitFullChangeSet = new BackendModelSingleCommitFullChangeSet();

	public uiFileChangeSetsNew: UiFileChangeSetModel[] = [];
	
	
	public uiRevisionData: BackendModelProjectRecentCommitRevision = new BackendModelProjectRecentCommitRevision();
	

    constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private navigationBarService : NavigationBarService,
		// actually this is not nice here, anyhow.... DIRTY HACK.
		private encoder: BpeEncoderProviderService,
		private route: ActivatedRoute, 
		private modalService: NgbModal 
	) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		this.activeRevisionID = this.route.snapshot.paramMap.get('revisionid');
	
		// TODO: query the revision change to previous revision from backend.
		this.projectDataQueryBackend.getRecentProjectRevisionDiffFullChangeSet(this.activeProjectID,this.activeRevisionID).subscribe(
			data => this.onSingeRevisionDiffProvided(data),
			error => console.log(error)
		);
		
		this.projectDataQueryBackend.getRecentProjectRevisionFilePathsData(this.activeProjectID,this.activeRevisionID).subscribe(
			data => this.onFileListActionsProvided(TransformCommitRevision.convertToUiReviewFileinformationArray(data)),
			error => console.log(error)
		);
		
		// TOOD: maybe combine with  ""getRecentProjectRevisionDiffFullChangeSet""
		this.projectDataQueryBackend.getRecentProjectRevisionInformation(this.activeProjectID,this.activeRevisionID).subscribe(
			data => this.onRevisionInformationProvided(data),
			error => console.log(error)
		);
		
	}

	onSingeRevisionDiffProvided( diffData: BackendModelSingleCommitFullChangeSet):void {
		this.uiModelSingleRevisionDiffs = diffData;
		
		this.uiFileChangeSetsNew=diffData.fileChangeSet.map(
				(item)=> TransformChangeSet.fromBackendFileChangeSetToUiFileChangeSet(item)
			);
	}
	
	setNavigation(): void {
		let x = []
		
		x.push(this.navigationBarService.createItem( this.activeProjectID, ['/',this.activeProjectID], false ));
		x.push(this.navigationBarService.createItem( this.uiRevisionData.shortrev, ['/',this.activeProjectID, 'revision', this.activeRevisionID], true ));
		
		this.navigationBarService.setBreadcrumbNavigation(x);
	}
	
	onRevisionInformationProvided( revisionData: BackendModelProjectRecentCommitRevision): void {
		this.uiRevisionData = revisionData;
		
		this.setNavigation();
	}
	
	
	onFileListActionsProvided( fileInformations : UiReviewFileInformation[]) : void {
		this.uiFileInformations = fileInformations;
	}

	// open side by side dialog
	openSideBySideDialog( filechangeSet:UiFileChangeSetModel ):void {
		const modalref = this.modalService.open(  SingleRevisionSideBySideDialogComponent,  {centered: true, ariaLabelledBy: 'modal-basic-title', size:<any>'fs'}    )
		
		modalref.componentInstance.setAllChangeSets(this.uiFileChangeSetsNew);
		modalref.componentInstance.setSelectedFileChangeSet(filechangeSet);
		
		modalref.result.then((result) => {
			result.subscribe(
				data => {} ,
				error => {}
			)
		}, (reason) => {
		  // this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
	 	});
	}
	
	// open experimental side by side dialog
	openExperimentalSideBySideDialog( filechangeSet:UiFileChangeSetModel ):void {
		const modalref = this.modalService.open(  ExperimentalSingleRevisionSideBySideDialogComponent,  {centered: true, ariaLabelledBy: 'modal-basic-title', size:<any>'fs'}    )
		
		modalref.componentInstance.setAllChangeSets(this.uiFileChangeSetsNew);
		modalref.componentInstance.setSelectedFileChangeSet(filechangeSet);
		
		modalref.result.then((result) => {
			result.subscribe(
				data => {} ,
				error => {}
			)
		}, (reason) => {
		  // this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
	 	});
	}

}
