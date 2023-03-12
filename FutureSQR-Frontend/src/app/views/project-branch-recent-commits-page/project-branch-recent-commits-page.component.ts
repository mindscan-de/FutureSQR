import { KeyValue } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';
import { NavigationBarService } from '../../uiservices/navigation-bar.service';

// BackendModel - should be actually a ui model 
import { BackendModelProjectRecentCommits } from '../../backend/model/backend-model-project-recent-commits';
import { BackendModelProjectRecentCommitRevision } from '../../backend/model/backend-model-project-recent-commit-revision';


@Component({
  selector: 'app-project-branch-recent-commits-page',
  templateUrl: './project-branch-recent-commits-page.component.html',
  styleUrls: ['./project-branch-recent-commits-page.component.css']
})
export class ProjectBranchRecentCommitsPageComponent implements OnInit {
	
	public uiModelRecentProjectCommitsGroupedByDate: Map<string, BackendModelProjectRecentCommits> = new Map<string, BackendModelProjectRecentCommits>();
	
	public activeProjectID: string = '';
    public activeBranchName: string;

	constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private navigationBarService : NavigationBarService,
		private route: ActivatedRoute
	) { }


	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		this.activeBranchName = this.route.snapshot.paramMap.get('branchname');
		
		// get project commits for a given branch.
/*		this.projectDataQueryBackend.getRecentProjectCommitsForBranch(this.activeProjectID, this.activeBranchName).subscribe( 
			data => this.onRecentProjectCommitsProvided(data),
			error => console.log(error)
		);
*/		
		
		
		this.setTopNavigation();
	}
	
	setTopNavigation() : void {
		let x = []
		
		x.push(this.navigationBarService.createItem( this.activeProjectID, ['/',this.activeProjectID], false ));
		x.push(this.navigationBarService.createItem( 'Branch', ['/', this.activeProjectID, 'branches'], false));
		x.push(this.navigationBarService.createItem( this.activeBranchName,['/', this.activeProjectID, 'branches'], true ));
		
		this.navigationBarService.setBreadcrumbNavigation(x);		
	}
	
	onRecentProjectCommitsProvided( recentProjectCommits: BackendModelProjectRecentCommits) : void {
		// this.uiModelRecentProjectCommitsGroupedByDate = this.m2mGroupByDateTransformer(recentProjectCommits);
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
	
	public originalOrder = (a: KeyValue<number,string>, b: KeyValue<number,string>): number => {
	  return 0;
	}	

	refreshCommitHistory(event:any) : void {
/*		this.projectDataQueryBackend.getRecentProjectCommitsForBranch(this.activeProjectID, this.activeBranchName).subscribe( 
			data => this.onRecentProjectCommitsProvided(data),
			error => console.log(error)
		);
*/	}
	
}
