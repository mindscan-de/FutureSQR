import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';
import { NavigationBarService } from '../../uiservices/navigation-bar.service';



@Component({
  selector: 'app-project-branches-page',
  templateUrl: './project-branches-page.component.html',
  styleUrls: ['./project-branches-page.component.css']
})
export class ProjectBranchesPageComponent implements OnInit {

	public activeProjectID: string = '';
	public projectBranches: string[] = ["main","authn_rb","configUser_showNameInNav_rb","main","waitstate_old_rb"];
	

	constructor(
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private navigationBarService : NavigationBarService,
		private route: ActivatedRoute, 
		private router: Router
	) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');

		this.setTopNavigation();
	}
	
	setTopNavigation() : void {
		let x = []
		
		x.push(this.navigationBarService.createItem( this.activeProjectID, ['/',this.activeProjectID], false ));
		x.push(this.navigationBarService.createItem( 'Reviews', ['/', this.activeProjectID, 'reviews'], false));
		x.push(this.navigationBarService.createItem( 'Branches', ['/', this.activeProjectID, 'branches'], true))
		
		
		this.navigationBarService.setBreadcrumbNavigation(x);		
	}
	

	reloadCommitHistory(event:any) : void {
		// Do nothing here, this button will be removed when project has database
		// and we switch from pull principle to push to database principle.
	}

}
