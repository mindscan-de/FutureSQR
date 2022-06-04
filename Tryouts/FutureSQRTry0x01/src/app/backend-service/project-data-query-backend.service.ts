import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

// ---- Import the interesting backend models

import { BackendModelProjectItem } from './project-model/backend-model-project-item';

@Injectable({
  providedIn: 'root'
})
export class ProjectDataQueryBackendService {
	
	private static readonly URL_GET_ALL_PROJECTS = "/assets/allprojects/mockup_allprojects.json";
	private static readonly URL_GET_MY_STARRED_PROJECTS = "/assets/allprojects/mockup_mystarredprojects.json";

    constructor(private httpClient : HttpClient ) { }

    getAllProjects () : Observable<BackendModelProjectItem[]> {
	    return this.httpClient.get<BackendModelProjectItem[]>(ProjectDataQueryBackendService.URL_GET_ALL_PROJECTS, {});
    }

	getMyStarredProjects() : Observable<BackendModelProjectItem[]> {
		return this.httpClient.get<BackendModelProjectItem[]>(ProjectDataQueryBackendService.URL_GET_MY_STARRED_PROJECTS, {});
	}
}
