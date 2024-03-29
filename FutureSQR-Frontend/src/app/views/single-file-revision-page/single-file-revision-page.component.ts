import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// backend services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// internal services
import { NavigationBarService } from '../../uiservices/navigation-bar.service';

// M2M Transformation
import { TransformCommitRevision } from '../../m2m/transform-commit-revision';


// BackendModel - should be actually a ui model
import { BackendModelProjectRecentCommitRevision } from '../../backend/model/backend-model-project-recent-commit-revision';
import { BackendModelRevisionFileContent } from '../../backend/model/backend-model-revision-file-content';
import { BackendModelSingleFileCommitHistory } from '../../backend/model/backend-model-single-file-commit-history';

// ui Model
import { UiReviewFileInformation } from '../../commonui/uimodel/ui-review-file-information';

@Component({
  selector: 'app-single-file-revision-page',
  templateUrl: './single-file-revision-page.component.html',
  styleUrls: ['./single-file-revision-page.component.css']
})
export class SingleFileRevisionPageComponent implements OnInit {
	
	public activeProjectID: string = '';
	public activeRevisionID: string = '';
	public activeFilePath: string = '';
	public uiActiveFileInformation: UiReviewFileInformation = new UiReviewFileInformation(".html","",true);
	// TODO: make this a ui model in future
	public uiOtherFileRevisions: BackendModelProjectRecentCommitRevision[] = [];
	// TODO: make this a ui model in future
	public uiOtherFileCommits: BackendModelSingleFileCommitHistory = new BackendModelSingleFileCommitHistory();
	public uiFileInformations: UiReviewFileInformation[] = [];
	
	// TODO: make this a ui model in future.
	public uiRevisionData: BackendModelProjectRecentCommitRevision = new BackendModelProjectRecentCommitRevision();
	public uiRevisionFileContent: BackendModelRevisionFileContent = new BackendModelRevisionFileContent(); 
	

	constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private navigationBarService : NavigationBarService,
		private route: ActivatedRoute
	) { 
		
		
	}

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		this.activeRevisionID = this.route.snapshot.paramMap.get('revisionid');
		this.activeFilePath = this.route.snapshot.queryParamMap.get('p');
		this.uiActiveFileInformation = new UiReviewFileInformation(this.activeFilePath, "", true);
		
		// provide info for "other changed files in revision" panel 
		this.projectDataQueryBackend.getRecentProjectRevisionFilePathsData(this.activeProjectID,this.activeRevisionID).subscribe(
			data => this.onFileListActionsProvided(TransformCommitRevision.convertToUiReviewFileinformationArray(data)),
			error => console.log(error)
		);
		
		// provide commit details
		this.projectDataQueryBackend.getRecentProjectRevisionInformation(this.activeProjectID,this.activeRevisionID).subscribe(
			data => this.onRevisionInformationProvided(data),
			error => console.log(error)
		);

		// provide file content
		this.projectDataQueryBackend.getParticularFileRevisionContent(this.activeProjectID, this.activeRevisionID, this.activeFilePath).subscribe(
			data => this.onFileContentForRevisionProvided(data),
			error => console.log(error)
		);
		
		// provide file history
		this.projectDataQueryBackend.getParticularFileCommitHistory(this.activeProjectID, this.activeFilePath).subscribe(
			data => this.onFileHistoryProvided(data),
			error => console.log(error)
		);
		
		// subscription when filepath changes - so we can load a different file.
		this.route.queryParams.subscribe(queryParams => this.onUpdateQuery(queryParams.p));
		
		// TODO? subscription when paramMap changes - so we can load a different revision and revision content.
		// TODO: we eed to subscribe to different revisions here....as well 
	}
	
	onFileListActionsProvided( fileInformations : UiReviewFileInformation[]) : void {
		this.uiFileInformations = fileInformations;
	}
	
	
	onRevisionInformationProvided( revisionData: BackendModelProjectRecentCommitRevision): void {
		this.uiRevisionData = revisionData;
		
		this.setNavigation();
	}
	
	onFileContentForRevisionProvided( revisionFileContent: BackendModelRevisionFileContent): void {
		this.uiRevisionFileContent = revisionFileContent;
		
		this.activeFilePath = revisionFileContent.filePath;
		this.uiActiveFileInformation = new UiReviewFileInformation(this.activeFilePath, "", true);
		
		this.setNavigation();
	}
	
	onFileHistoryProvided( fileCommitHistory: BackendModelSingleFileCommitHistory): void {
		// TODO: transform to uiModel.
		this.uiOtherFileRevisions = fileCommitHistory.revisions;
	}
	
	onUpdateQuery(newPath:string): void {
		// we actually need to update the file history also, when we access a different file.
		this.projectDataQueryBackend.getParticularFileCommitHistory(this.activeProjectID, newPath).subscribe(
			data => this.onFileHistoryProvided(data),
			error => console.log(error)
		);
		
		
		this.projectDataQueryBackend.getParticularFileRevisionContent(this.activeProjectID, this.activeRevisionID, newPath).subscribe(
			data => this.onFileContentForRevisionProvided(data),
			error => console.log(error)
		);
	}
	
	setNavigation(): void {
		let x = []
		
		x.push(this.navigationBarService.createItem( this.activeProjectID, ['/',this.activeProjectID], false ));
		x.push(this.navigationBarService.createItem( this.uiRevisionData.shortrev, ['/',this.activeProjectID, 'revision', this.activeRevisionID], false ));
		x.push(this.navigationBarService.createItemWithQueryParameters( this.activeFilePath,['/',this.activeProjectID, 'revision', this.activeRevisionID, 'viewfile'], {'p':this.activeFilePath}, true));
		
		this.navigationBarService.setBreadcrumbNavigation(x);
	}

}
