import { KeyValue } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';
import { NavigationBarService } from '../../uiservices/navigation-bar.service';


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

	constructor( 
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private navigationBarService : NavigationBarService,
		private route: ActivatedRoute, 
		private router: Router
	) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		
		this.projectDataQueryBackend.getRecentProjectCommits(this.activeProjectID).subscribe( 
			data => this.onRecentProjectCommitsProvided(data),
			error => console.log(error)
		);
		
		this.setTopNavigation();
	}
	
	setTopNavigation() : void {
		let x = []
		
		x.push(this.navigationBarService.createItem( this.activeProjectID, ['/',this.activeProjectID], true ));
		x.push(this.navigationBarService.createItem( 'Reviews', ['/', this.activeProjectID, 'reviews'], false));
		
		this.navigationBarService.setBreadcrumbNavigation(x);		
	}

	onRecentProjectCommitsProvided( recentProjectCommits: BackendModelProjectRecentCommits) : void {
		this.uiModelRecentProjectCommitsGroupedByDate = this.m2mGroupByDateTransformer(recentProjectCommits);
	}


	public originalOrder = (a: KeyValue<number,string>, b: KeyValue<number,string>): number => {
	  return 0;
	}	
	
	m2mGroupByDateTransformer(ungrouped: BackendModelProjectRecentCommits): Map<string, BackendModelProjectRecentCommits> {
		var grouped = new Map<string, BackendModelProjectRecentCommits>();
		
		// initialize with an illegal date first / instead of empty.
		var previousdate = "1999-13-32";
		
		// iterate grouped and 
		for(var i:number = 0;i<ungrouped.revisions.length;i++) {
			var currentCommit:BackendModelProjectRecentCommitRevision = ungrouped.revisions[i];
			
			if(currentCommit.shortdate != previousdate) {
				previousdate = currentCommit.shortdate;
				grouped.set(previousdate, new BackendModelProjectRecentCommits());
			}
			
			grouped.get(previousdate).revisions.push(currentCommit);
		}
		
		return grouped;
	}
	
	reloadCommitHistory(event:any) : void {
		this.projectDataQueryBackend.getRecentProjectCommits(this.activeProjectID).subscribe( 
			data => this.onRecentProjectCommitsProvided(data),
			error => console.log(error)
		);
	}
	
}
