import { Component, OnInit } from '@angular/core';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';

// BackendModel - should be actually a ui model 
import { BackendModelProjectRecentCommits } from '../../backend/model/backend-model-project-recent-commits';


@Component({
  selector: 'app-project-recent-commits-page',
  templateUrl: './project-recent-commits-page.component.html',
  styleUrls: ['./project-recent-commits-page.component.css']
})
export class ProjectRecentCommitsPageComponent implements OnInit {

  public uiModelRecentProjectCommits: BackendModelProjectRecentCommits = new BackendModelProjectRecentCommits();

  constructor( private projectDataQueryBackend : ProjectDataQueryBackendService ) { }

  ngOnInit(): void {
	this.projectDataQueryBackend.getRecentProjectCommits("").subscribe( 
		data => this.onRecentProjectCommitsProvided(data),
		error => console.log(error)
	);
  }

  onRecentProjectCommitsProvided( recentProjectCommits: BackendModelProjectRecentCommits) : void {
	this.uiModelRecentProjectCommits = recentProjectCommits;
  }

}
