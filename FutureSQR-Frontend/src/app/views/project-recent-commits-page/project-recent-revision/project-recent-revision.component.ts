import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';

// Services
import { ProjectDataQueryBackendService } from '../../../backend/services/project-data-query-backend.service';


@Component({
  selector: 'app-project-recent-revision',
  templateUrl: './project-recent-revision.component.html',
  styleUrls: ['./project-recent-revision.component.css']
})
export class ProjectRecentRevisionComponent implements OnInit {

	@Input() activeProjectID : string;
	@Input() activeRevision : any;
	
	public showFileList: boolean = false;
	
	constructor( private projectDataQueryBackend : ProjectDataQueryBackendService, private router: Router  ) { }

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
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
	
	// TODO: retrieve file list for this version
	// Open Close filelist
	onToggleFileList(): void {
		this.showFileList = !this.showFileList;
	}
}
