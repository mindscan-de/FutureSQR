import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// Backend Model
import { BackendModelReviewData } from '../../backend/model/backend-model-review-data';

import { BackendModelSingleCommitFullChangeSet } from '../../backend/model/backend-model-single-commit-full-change-set';
import { BackendModelSingleCommitFileChangeSet } from '../../backend/model/backend-model-single-commit-file-change-set';
import { BackendModelSingleCommitFileActionsInfo } from '../../backend/model/backend-model-single-commit-file-actions-info';


@Component({
  selector: 'app-single-review-page',
  templateUrl: './single-review-page.component.html',
  styleUrls: ['./single-review-page.component.css']
})
export class SingleReviewPageComponent implements OnInit {
	
	public activeProjectID: string = '';
	public activeReviewID: string = '';
	public uiFilePathActions: string[][] = [];

    public uiModelSingleRevisionDiffs: BackendModelSingleCommitFullChangeSet = new BackendModelSingleCommitFullChangeSet();
	public uiFileChangeSets: BackendModelSingleCommitFileChangeSet[] = [];
	
	public activeReviewData: BackendModelReviewData = new BackendModelReviewData();

	constructor( private projectDataQueryBackend : ProjectDataQueryBackendService, private route: ActivatedRoute ) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		this.activeReviewID = this.route.snapshot.paramMap.get('reviewid');
		
		// TODO: query the review information
		this.projectDataQueryBackend.getReviewData(this.activeProjectID, this.activeReviewID).subscribe(
			data => this.onReviewDataReceived(data),
			error => {}			
		);
		
		// TODO: query some revision infromation for this particular review
		// TODO: query the filelist for this review
		this.projectDataQueryBackend.getReviewFilePathsData(this.activeProjectID,this.activeReviewID ).subscribe (
			data => this.onFileListActionsProvided(data),
			error => console.log(error)
		);

		this.projectDataQueryBackend.getReviewRevisionDiffFullChangeSet(this.activeProjectID,this.activeReviewID ).subscribe(
			data => this.onDiffDataReceived(data),
			error => {}
		);
	}
	
	onReviewDataReceived(reviewData: BackendModelReviewData):  void {
		this.activeReviewData = reviewData;
	}
	
	onDiffDataReceived( diffData: BackendModelSingleCommitFullChangeSet ):void {
		this.uiModelSingleRevisionDiffs = diffData;
		this.uiFileChangeSets = diffData.fileChangeSet;
	}


	onFileListActionsProvided( fileChanges: BackendModelSingleCommitFileActionsInfo) : void {
		this.uiFilePathActions = fileChanges.fileActionMap;
	}

}
