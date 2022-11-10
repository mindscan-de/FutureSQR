import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

// Services
import { ProjectDataQueryBackendService } from '../../backend/services/project-data-query-backend.service';
import { NavigationBarService } from '../../uiservices/navigation-bar.service';


// backend model -> should be a ui model.
import { BackendModelReviewData } from '../../backend/model/backend-model-review-data';
import { BackendModelProjectRecentReviews } from '../../backend/model/backend-model-project-recent-reviews';


@Component({
  selector: 'app-project-recent-reviews-page',
  templateUrl: './project-recent-reviews-page.component.html',
  styleUrls: ['./project-recent-reviews-page.component.css']
})
export class ProjectRecentReviewsPageComponent implements OnInit {
	
	public uiModelRecentProjectReviews:BackendModelReviewData[] = [];
	public uiModelRecentClosedProjectReviews:BackendModelReviewData[] = [];
	public activeProjectID: string = '';


	constructor( 
		private projectDataQueryBackend : ProjectDataQueryBackendService,
		private navigationBarService : NavigationBarService,
		private route: ActivatedRoute 
	) { }

	ngOnInit(): void {
		this.activeProjectID = this.route.snapshot.paramMap.get('projectid');
		
		this.projectDataQueryBackend.getRecentReviewsByProject(this.activeProjectID).subscribe (
			data => this.onRecentReviewsLoaded(data),
			error => {}
		);
		
		// add navigation
		let x = []
		x.push(this.navigationBarService.createItem( this.activeProjectID, ['/',this.activeProjectID], false ));
		x.push(this.navigationBarService.createItem( 'Reviews', ['/',this.activeProjectID, 'reviews'], false ));
		this.navigationBarService.setBreadcrumbNavigation(x);		
		
	}
	
	onRecentReviewsLoaded(recentReviews:BackendModelProjectRecentReviews) : void {
		this.uiModelRecentProjectReviews = recentReviews.openReviews;
		this.uiModelRecentClosedProjectReviews = recentReviews.recentClosedReviews;
	}
	
	onReviewUpdated(event: string): void {
		this.projectDataQueryBackend.getRecentReviewsByProject(this.activeProjectID).subscribe (
					data => this.onRecentReviewsLoaded(data),
					error => {}
				);		
	}

}
