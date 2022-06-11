import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

// ---- Import the interesting backend models

import { BackendModelProjectItem } from '../model/backend-model-project-item';
import { BackendModelProjectRecentCommits } from '../model/backend-model-project-recent-commits';
import { BackendModelProjectSingleRevisonDiff } from '../model/backend-model-project-single-revison-diff';

@Injectable({
  providedIn: 'root'
})
export class ProjectDataQueryBackendService {
	
	private static readonly URL_GET_ALL_PROJECTS:string           = "/FutureSQR/rest/user/allaccessibleprojetcs";
	private static readonly URL_GET_MY_STARRED_PROJECTS:string    = "/FutureSQR/rest/user/starredprojects";
	// TODO: this should be cooler
	private static readonly URL_GET_RECENT_PROJECT_COMMITS:string = "/FutureSQR/rest/project/${projectid}/recentcommits";
	private static readonly URL_GET_PROJECT_REVISION_DIFF:string  = "/FutureSQR/rest/project/${projectid}/revisiondiff/${revisionid}"

    constructor(private httpClient : HttpClient ) { }

    getAllProjects () : Observable<BackendModelProjectItem[]> {
	    return this.httpClient.get<BackendModelProjectItem[]>(ProjectDataQueryBackendService.URL_GET_ALL_PROJECTS, {});
    }

	// Actually the starred projects depend on the users choices
	getMyStarredProjects() : Observable<BackendModelProjectItem[]> {
		// TODO: provide USER-UUID / username
		return this.httpClient.get<BackendModelProjectItem[]>(ProjectDataQueryBackendService.URL_GET_MY_STARRED_PROJECTS, {});
	}
	
	getRecentProjectCommits(projectid:string) : Observable<BackendModelProjectRecentCommits> {
		var url = `/FutureSQR/rest/project/${projectid}/recentcommits`;
		return this.httpClient.get<BackendModelProjectRecentCommits>(url, {});
	}
	
	getRecentProjectRevisionDiff(projectid:string, revisionid:string): Observable<BackendModelProjectSingleRevisonDiff> {
		var url = `/FutureSQR/rest/project/${projectid}/revisiondiff/${revisionid}`;
		return this.httpClient.get<BackendModelProjectSingleRevisonDiff>(url, {});
	}
}
