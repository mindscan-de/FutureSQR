import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

// ---- Import the interesting backend models

import { BackendModelProjectItem } from '../model/backend-model-project-item';
import { BackendModelProjectRecentCommits } from '../model/backend-model-project-recent-commits';
import { BackendModelProjectRecentReviews } from '../model/backend-model-project-recent-reviews';
import { BackendModelSingleCommitFullChangeSet } from '../model/backend-model-single-commit-full-change-set'; 
import { BackendModelSingleCommitFileActionsInfo } from '../model/backend-model-single-commit-file-actions-info';
import { BackendModelCreateReviewResult } from '../model/backend-model-create-review-result';
import { BackendModelReviewData } from '../model/backend-model-review-data';
import { BackendModelProjectSimpleInformation } from '../model/backend-model-project-simple-information';


@Injectable({
  providedIn: 'root'
})
export class ProjectDataQueryBackendService {
	
	private static readonly URL_GET_ALL_PROJECTS:string           = "/FutureSQR/rest/user/allaccessibleprojetcs";
	private static readonly URL_GET_MY_STARRED_PROJECTS:string    = "/FutureSQR/rest/user/starredprojects";
	// TODO: this should be cooler
	private static readonly URL_GET_RECENT_PROJECT_COMMITS:string = "/FutureSQR/rest/project/${projectid}/recentcommits";
	private static readonly URL_GET_PROJECT_REVISION_DIFF:string  = "/FutureSQR/rest/project/${projectid}/revisiondiff/${revisionid}";
	private static readonly URL_GET_PROJECT_REVISION_FILES:string = "/FutureSQR/rest/project/${projectid}/filelist/${revisionid}";

    constructor(private httpClient : HttpClient ) { }

    getAllProjects () : Observable<BackendModelProjectItem[]> {
	    return this.httpClient.get<BackendModelProjectItem[]>(ProjectDataQueryBackendService.URL_GET_ALL_PROJECTS, {});
    }

	// Actually the starred projects depend on the users choices
	getMyStarredProjects() : Observable<BackendModelProjectItem[]> {
		// TODO: provide USER-UUID / username
		return this.httpClient.get<BackendModelProjectItem[]>(ProjectDataQueryBackendService.URL_GET_MY_STARRED_PROJECTS, {});
	}
	
	starProject(projectid:string) : Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/star`;
		let formdata = new FormData();
		
		// TODO later
		formdata.append('userid','');
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	unstarProject(projectid:string) : Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/unstar`;
		let formdata = new FormData();
		
		// TODO later
		formdata.append('userid','');
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	
	
	getRecentProjectCommits(projectid:string) : Observable<BackendModelProjectRecentCommits> {
		var url = `/FutureSQR/rest/project/${projectid}/recentcommits`;
		return this.httpClient.get<BackendModelProjectRecentCommits>(url, {});
	}
	
	
	getRecentProjectCommutsSinceRevision(projectid: string, revisionid:string):Observable<BackendModelProjectRecentCommits> {
		var url = `/FutureSQR/rest/project/${projectid}/recentcommitsfromrevid/${revisionid}`;
		return this.httpClient.get<BackendModelProjectRecentCommits>(url, {});
	} 	
	
	getRecentProjectRevisionDiffFullChangeSet(projectid:string, revisionid:string): Observable<BackendModelSingleCommitFullChangeSet> {
		var url = `/FutureSQR/rest/project/${projectid}/revisiondiff/${revisionid}`;
		return this.httpClient.get<BackendModelSingleCommitFullChangeSet>(url, {});
	}
	
	getReviewRevisionDiffFullChangeSet(projectid:string, reviewid:string) : Observable<BackendModelSingleCommitFullChangeSet> {
		var url = `/FutureSQR/rest/project/${projectid}/reviewdiff/${reviewid}`;
		return this.httpClient.get<BackendModelSingleCommitFullChangeSet>(url, {});
	}
	
	getRecentProjectRevisionFilePathsData(projectid:string, revisionid:string): Observable<BackendModelSingleCommitFileActionsInfo> {
		var url = `/FutureSQR/rest/project/${projectid}/revisionfilelist/${revisionid}`;
		return this.httpClient.get<BackendModelSingleCommitFileActionsInfo>(url, {});
	}
	
	getReviewFilePathsData(projectid:string, reviewid:string): Observable<BackendModelSingleCommitFileActionsInfo> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/filelist`;
		return this.httpClient.get<BackendModelSingleCommitFileActionsInfo>(url, {});
	}
	
	getReviewData(projectid:string, reviewid:string): Observable<BackendModelReviewData> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/information`
		return this.httpClient.get<BackendModelReviewData>(url, {});
	}
	
	getRecentReviewsByProject(projectid:string): Observable<BackendModelProjectRecentReviews> {
		var url = `/FutureSQR/rest/project/${projectid}/recentreviews`;
		
		// returns actually a list of open reviews for this project with some useful info....
		return this.httpClient.get<BackendModelProjectRecentReviews>(url, {});
	}
	
	getSimpleInformationByProject(projectid:string) : Observable<BackendModelProjectSimpleInformation> {
		var url = `/FutureSQR/rest/project/${projectid}/information`;
		
		return this.httpClient.get<BackendModelProjectSimpleInformation>(url, {});
	}
	
	createNewReview(projectid:string, revisionid:string): Observable<BackendModelCreateReviewResult> {
		var url = `/FutureSQR/rest/project/${projectid}/review/create`;
		let formdata = new FormData();
		
		formdata.append('revisionid', revisionid);
		
		return this.httpClient.post<BackendModelCreateReviewResult>(url, formdata);
	}
	
	
	closeReview(projectid:string, reviewid:string): Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/review/close`;
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	reopenReview(projectid:string, reviewid:string): Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/review/reopen`;
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	appendReviewWithRevision(projectid: string, reviewid:string, revisionid): Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/apppend`;
		
		let formdata = new FormData();
		formdata.append('reviewid',reviewid);
		formdata.append('revisionid',revisionid);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	deleteReview(projectid:string, reviewid:string): Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/review/delete`;
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	addReviewer(projectid:string, reviewid:string, reviewerid:string): Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/addreviewer`;
		
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		formdata.append('reviewerid', reviewerid);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	approveReview(projectid: string, reviewid:string, reviewerid:string) : Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/approvereview`;
		
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		formdata.append('reviewerid', reviewerid);
		
		return this.httpClient.post<any>(url,formdata);
	}


	concernReview(projectid: string, reviewid:string, reviewerid:string) : Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/concernreview`;
		
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		formdata.append('reviewerid', reviewerid);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
}
