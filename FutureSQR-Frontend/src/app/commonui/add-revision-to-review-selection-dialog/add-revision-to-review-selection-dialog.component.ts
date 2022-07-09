import { KeyValue } from '@angular/common';
import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// should be a uimodel instead of a backend model
import { BackendModelReviewData } from '../../backend/model/backend-model-review-data';
import { BackendModelProjectRecentCommits } from '../../backend/model/backend-model-project-recent-commits';
import { BackendModelProjectRecentCommitRevision } from '../../backend/model/backend-model-project-recent-commit-revision';


@Component({
  selector: 'app-add-revision-to-review-selection-dialog',
  templateUrl: './add-revision-to-review-selection-dialog.component.html',
  styleUrls: ['./add-revision-to-review-selection-dialog.component.css']
})
export class AddRevisionToReviewSelectionDialogComponent implements OnInit {

	public uiModelRecentProjectCommitsGroupedByDate: Map<string, BackendModelProjectRecentCommits> = new Map<string, BackendModelProjectRecentCommits>();
	public currentUiReviewData : BackendModelReviewData = new BackendModelReviewData();

	constructor(private projectDataQueryBackend : ProjectDataQueryBackendService, public activeModal: NgbActiveModal) { }

	ngOnInit(): void {
	}

	setActiveReviewData(activeReviewData: BackendModelReviewData): void {
		this.currentUiReviewData = activeReviewData;
		
		// TODO collect the project commits groupted by date, without those
		//      which are already connected to reviews.
		// console.log(activeReviewData);
		
		this.projectDataQueryBackend.getRecentProjectCommutsSinceRevision( activeReviewData.reviewFkProjectId, activeReviewData.reviewRevisions[0]).subscribe (
			data => this.onRevisionDataProvided(data) ,
			error => {}			
		);
	}
	
	onRevisionDataProvided(ungrouped:BackendModelProjectRecentCommits) : void {
		this.uiModelRecentProjectCommitsGroupedByDate = this.m2mGroupByDateTransformer(ungrouped);
	}
	
	addRevisionToReview(revisionid): void {
		
	}
	
	
	public originalOrder = (a: KeyValue<number,string>, b: KeyValue<number,string>): number => {
	  return 0;
	}
	
	m2mGroupByDateTransformer(ungrouped: BackendModelProjectRecentCommits): Map<string, BackendModelProjectRecentCommits> {
		var grouped = new Map<string, BackendModelProjectRecentCommits>();
		
		// initialize with an illegal date first / instead of empty.
		var previousdate = "1999-13-32";
		
		// iterate grouped and 
		for(var i:number = 1;i<ungrouped.revisions.length;i++) {
			var currentCommit:BackendModelProjectRecentCommitRevision = ungrouped.revisions[i];
			
			if(currentCommit.shortdate != previousdate) {
				previousdate = currentCommit.shortdate;
				grouped.set(previousdate, new BackendModelProjectRecentCommits());
			}
			
			grouped.get(previousdate).revisions.push(currentCommit);
		}
		
		return grouped;
	}
	
	
}
