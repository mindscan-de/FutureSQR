import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';


// BackendModel - should be actually a ui model 
import { BackendModelSingleCommitFullChangeSet } from '../../backend/model/backend-model-single-commit-full-change-set';
import { BackendModelSingleCommitFileChangeSet } from '../../backend/model/backend-model-single-commit-file-change-set';
import { BackendModelSingleCommitFileActionsInfo } from '../../backend/model/backend-model-single-commit-file-actions-info';


@Component({
  selector: 'app-single-revision-page',
  templateUrl: './single-revision-page.component.html',
  styleUrls: ['./single-revision-page.component.css']
})
export class SingleRevisionPageComponent implements OnInit {
	
	public activeProjectID: string = '';
	public activeRevisionID: string = '';
    public uiModelSingleRevisionDiffs: BackendModelSingleCommitFullChangeSet = new BackendModelSingleCommitFullChangeSet();
	public uiFileChangeSets: BackendModelSingleCommitFileChangeSet[] = [];
	public uiFilePathActions: string[][] = [];

    constructor(private projectDataQueryBackend : ProjectDataQueryBackendService, private route: ActivatedRoute  ) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		this.activeRevisionID = this.route.snapshot.paramMap.get('revisionid');
	
		// TODO: query the revision change to previous revision from backend.
		this.projectDataQueryBackend.getRecentProjectRevisionDiffFullChangeSet(this.activeProjectID,this.activeRevisionID).subscribe(
			data => this.onSingeRevisionDiffProvided(data),
			error => console.log(error)
		);
		
		this.projectDataQueryBackend.getRecentProjectRevisionFilePathsData(this.activeProjectID,this.activeRevisionID).subscribe(
			data => this.onFileListActionsProvided(data),
			error => console.log(error)
			);
	}

	onSingeRevisionDiffProvided( diffData: BackendModelSingleCommitFullChangeSet):void {
		this.uiModelSingleRevisionDiffs = diffData;
		this.uiFileChangeSets = diffData.fileChangeSet;
	}
	
	
	onFileListActionsProvided( fileChanges: BackendModelSingleCommitFileActionsInfo) : void {
		this.uiFilePathActions = fileChanges.fileActionMap;
	}

}
