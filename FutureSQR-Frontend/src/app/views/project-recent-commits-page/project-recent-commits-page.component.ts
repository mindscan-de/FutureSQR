import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// BackendModel - should be actually a ui model 
import { BackendModelProjectRecentCommits } from '../../backend/model/backend-model-project-recent-commits';

/*
 * We want to group changes by date, therefore we might need the relative date as well.

 */

@Component({
  selector: 'app-project-recent-commits-page',
  templateUrl: './project-recent-commits-page.component.html',
  styleUrls: ['./project-recent-commits-page.component.css']
})
export class ProjectRecentCommitsPageComponent implements OnInit {

  public uiModelRecentProjectCommits: BackendModelProjectRecentCommits = new BackendModelProjectRecentCommits();
  public activeProjectID: string = '';

  constructor( private projectDataQueryBackend : ProjectDataQueryBackendService, private route: ActivatedRoute  ) { }

  ngOnInit(): void {
	this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
	
	this.projectDataQueryBackend.getRecentProjectCommits(this.activeProjectID).subscribe( 
		data => this.onRecentProjectCommitsProvided(data),
		error => console.log(error)
	);
  }

  onRecentProjectCommitsProvided( recentProjectCommits: BackendModelProjectRecentCommits) : void {
	this.uiModelRecentProjectCommits = recentProjectCommits;
  }

}
