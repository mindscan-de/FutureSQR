import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Services
import { NavigationBarService } from '../../uiservices/navigation-bar.service';


@Component({
  selector: 'app-project-branch-recent-commits-page',
  templateUrl: './project-branch-recent-commits-page.component.html',
  styleUrls: ['./project-branch-recent-commits-page.component.css']
})
export class ProjectBranchRecentCommitsPageComponent implements OnInit {
	
	public activeProjectID: string = '';
    public activeBranchName: string;

	constructor(
		private navigationBarService : NavigationBarService,
		private route: ActivatedRoute
	) { }


	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		this.activeBranchName = this.route.snapshot.paramMap.get('branchname');
		
		this.setTopNavigation();
	}
	
	setTopNavigation() : void {
		let x = []
		
		x.push(this.navigationBarService.createItem( this.activeProjectID, ['/',this.activeProjectID], false ));
		x.push(this.navigationBarService.createItem( 'Branch', ['/', this.activeProjectID, 'branches'], false));
		x.push(this.navigationBarService.createItem( this.activeBranchName,['/', this.activeProjectID, 'branches'], true ));
		
		this.navigationBarService.setBreadcrumbNavigation(x);		
	}
	


	refreshCommitHistory(event:any) : void {
/*		this.projectDataQueryBackend.getRecentProjectCommits(this.activeProjectID).subscribe( 
			data => this.onRecentProjectCommitsProvided(data),
			error => console.log(error)
		);
*/	}
	
}
