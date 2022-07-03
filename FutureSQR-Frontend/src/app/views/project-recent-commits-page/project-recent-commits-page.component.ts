import { KeyValue } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// BackendModel - should be actually a ui model 
import { BackendModelProjectRecentCommits } from '../../backend/model/backend-model-project-recent-commits';
import { BackendModelProjectRecentCommitRevision } from '../../backend/model/backend-model-project-recent-commit-revision';

/*
 * We want to group changes by date, therefore we might need the relative date as well.

 */

@Component({
  selector: 'app-project-recent-commits-page',
  templateUrl: './project-recent-commits-page.component.html',
  styleUrls: ['./project-recent-commits-page.component.css']
})
export class ProjectRecentCommitsPageComponent implements OnInit {

	public uiModelRecentProjectCommitsGroupedByDate: Map<string, BackendModelProjectRecentCommits> = new Map<string, BackendModelProjectRecentCommits>();
	public activeProjectID: string = '';

	constructor( private projectDataQueryBackend : ProjectDataQueryBackendService, private route: ActivatedRoute, private router: Router  ) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		
		this.projectDataQueryBackend.getRecentProjectCommits(this.activeProjectID).subscribe( 
			data => this.onRecentProjectCommitsProvided(data),
			error => console.log(error)
		);
	}

	onRecentProjectCommitsProvided( recentProjectCommits: BackendModelProjectRecentCommits) : void {
		this.uiModelRecentProjectCommitsGroupedByDate = this.m2mGroupByDateTransformer(recentProjectCommits);
	}

	onCreateReview(projectId: string, revisionId: string) : void {
		this.projectDataQueryBackend.createNewReview(projectId, revisionId).subscribe (
			data => {
				// TODO redirect o review page.
				this.router.navigate(['/', projectId, 'review', data['reviewId']]);
			},
			error => {}
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
