import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';


// BackendModel - should be actually a ui model 
import { BackendModelProjectSingleRevisonDiff } from '../../backend/model/backend-model-project-single-revison-diff';
import { BackendModelProjectSingleFileChangeItem } from '../../backend/model/backend-model-project-single-file-change-item';

@Component({
  selector: 'app-single-revision-page',
  templateUrl: './single-revision-page.component.html',
  styleUrls: ['./single-revision-page.component.css']
})
export class SingleRevisionPageComponent implements OnInit {
	
	public activeProjectID: string = '';
	public activeRevisionID: string = '';
    public uiModelSingleRevisionDiffs: BackendModelProjectSingleRevisonDiff = new BackendModelProjectSingleRevisonDiff();
	public uiChanges: BackendModelProjectSingleFileChangeItem[] = [];

    constructor(private projectDataQueryBackend : ProjectDataQueryBackendService, private route: ActivatedRoute  ) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		this.activeRevisionID = this.route.snapshot.paramMap.get('revisionid');
	
		// TODO: query the revision change to previous revision from backend.
		this.projectDataQueryBackend.getRecentProjectRevisionDiff(this.activeProjectID,this.activeRevisionID).subscribe(
			data => this.onSingeRevisionDiffProvided(data),
			error => console.log(error)
		)
	}

	onSingeRevisionDiffProvided( diffData: BackendModelProjectSingleRevisonDiff):void {
		this.uiModelSingleRevisionDiffs = diffData;
		this.uiChanges = diffData.changes;
	}

}
