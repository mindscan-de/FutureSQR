import { KeyValue } from '@angular/common';
import { Component, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

// Backend Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// Internal Services
import { CurrentUserService } from '../../services/current-user.service';

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
	public revisionChangedCallbackFkt : () => void = null;

	constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private currentUserService: CurrentUserService,
		public activeModal: NgbActiveModal
	) { }

	ngOnInit(): void {
	}

	setActiveReviewData(activeReviewData: BackendModelReviewData): void {
		this.currentUiReviewData = activeReviewData;
		
		// TODO collect the project commits groupted by date, without those
		//      which are already connected to reviews.
		// console.log(activeReviewData);
		
		this.projectDataQueryBackend.getRecentProjectCommitsSinceRevision( activeReviewData.reviewFkProjectId, activeReviewData.reviewRevisions[0]).subscribe (
			data => this.onRevisionDataProvided(data) ,
			error => {}			
		);
	}
	
	setRevisionConfigurationChangedCallback(fkt:() => void): void {
		this.revisionChangedCallbackFkt = fkt;
	}
	
	onRevisionDataProvided(ungrouped:BackendModelProjectRecentCommits) : void {
		this.uiModelRecentProjectCommitsGroupedByDate = this.m2mGroupByDateTransformer(ungrouped);
	}
	
	appendRevisionToReview(projectid, reviewid, revisionid): void {
		
		if(this.revisionChangedCallbackFkt != null) {
			this.revisionChangedCallbackFkt();
		}
		
		let currentUserId = this.currentUserService.getCurrentUserUUID();
		
		// TODO: lock tthis component until the append was successful
		this.projectDataQueryBackend.appendRevisionToReview( projectid, reviewid, revisionid, currentUserId).subscribe(
			data=>{
				// todo: unlock this component.				
				// todo: close dialog?
			},
			error=> {
				// todo: unlock this component.
			}			
		);
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
