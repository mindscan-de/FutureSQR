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
import { BackendModelProjectRecentCommitRevision } from '../model/backend-model-project-recent-commit-revision';
import { BackendModelThreadsData } from '../model/backend-model-threads-data';


/**
 * TODO: rework all subscription and post events to avoid any memory and/or resource leaks.
 */

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

	// TODO: rework this subscription to once.
    getAllProjects () : Observable<BackendModelProjectItem[]> {
	    return this.httpClient.get<BackendModelProjectItem[]>(ProjectDataQueryBackendService.URL_GET_ALL_PROJECTS, {});
    }

	// TODO: rework this subscription to once.
	// Actually the starred projects depend on the users choices
	getMyStarredProjects() : Observable<BackendModelProjectItem[]> {
		// TODO: provide USER-UUID / username
		return this.httpClient.get<BackendModelProjectItem[]>(ProjectDataQueryBackendService.URL_GET_MY_STARRED_PROJECTS, {});
	}
	
	// TODO: rework this subscription to once.
	starProject(projectid:string) : Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/star`;
		let formdata = new FormData();
		
		// TODO later
		formdata.append('userid','');
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	// TODO: rework this subscription to once.
	unstarProject(projectid:string) : Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/unstar`;
		let formdata = new FormData();
		
		// TODO later
		formdata.append('userid','');
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	// TODO: rework this subscription to once.
	updateProjectCache(projectid:string) : Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/updatecache`;
		let formdata = new FormData();
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	// TODO: rework this subscription to once.
	getRecentProjectCommits(projectid:string) : Observable<BackendModelProjectRecentCommits> {
		var url = `/FutureSQR/rest/project/${projectid}/recentcommits`;
		return this.httpClient.get<BackendModelProjectRecentCommits>(url, {});
	}
	
	// TODO: rework this subscription to once.
	getRecentProjectCommutsSinceRevision(projectid: string, revisionid:string):Observable<BackendModelProjectRecentCommits> {
		var url = `/FutureSQR/rest/project/${projectid}/recentcommitsfromrevid/${revisionid}`;
		return this.httpClient.get<BackendModelProjectRecentCommits>(url, {});
	} 	
	
	// TODO: rework this subscription to once.
	getRecentProjectRevisionDiffFullChangeSet(projectid:string, revisionid:string): Observable<BackendModelSingleCommitFullChangeSet> {
		var url = `/FutureSQR/rest/project/${projectid}/revisiondiff/${revisionid}`;
		return this.httpClient.get<BackendModelSingleCommitFullChangeSet>(url, {});
	}
	
	// TODO: rework this subscription to once.
	getReviewRevisionDiffFullChangeSet(projectid:string, reviewid:string) : Observable<BackendModelSingleCommitFullChangeSet> {
		var url = `/FutureSQR/rest/project/${projectid}/reviewdiff/${reviewid}`;
		return this.httpClient.get<BackendModelSingleCommitFullChangeSet>(url, {});
	}
	
	// TODO: rework this subscription to once.
	getRecentProjectRevisionFilePathsData(projectid:string, revisionid:string): Observable<BackendModelSingleCommitFileActionsInfo> {
		var url = `/FutureSQR/rest/project/${projectid}/revisionfilelist/${revisionid}`;
		return this.httpClient.get<BackendModelSingleCommitFileActionsInfo>(url, {});
	}
	
	// TODO: rework this subscription to once.
	getRecentProjectRevisionInformation(projectid:string, revisionid:string): Observable<BackendModelProjectRecentCommitRevision> {
		var url = `/FutureSQR/rest/project/${projectid}/revision/${revisionid}/information`;
		return this.httpClient.get<BackendModelProjectRecentCommitRevision>(url, {});
	}
	
	// TODO: rework this subscription to once.
	getReviewFilePathsData(projectid:string, reviewid:string): Observable<BackendModelSingleCommitFileActionsInfo> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/filelist`;
		return this.httpClient.get<BackendModelSingleCommitFileActionsInfo>(url, {});
	}
	
	// TODO: rework this subscription to once.
	getReviewData(projectid:string, reviewid:string): Observable<BackendModelReviewData> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/information`;
		return this.httpClient.get<BackendModelReviewData>(url, {});
	}
	
	// TODO: rework this subscription to once.
	getThreadData(projectid:string, reviewid:string): Observable<BackendModelThreadsData> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/threads`;
		return this.httpClient.get<BackendModelThreadsData>(url, {});
	}
	
	// TODO: rework this subscription to once.
	getReviewSimpleRevisionInformationList(projectid: string, reviewid: string): Observable <BackendModelProjectRecentCommitRevision[]> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/revisiondetails`
		return this.httpClient.get<BackendModelProjectRecentCommitRevision[]>(url, {});
	}
	
	// TODO: rework this subscription to once.
	getRecentReviewsByProject(projectid:string): Observable<BackendModelProjectRecentReviews> {
		var url = `/FutureSQR/rest/project/${projectid}/recentreviews`;
		
		// returns actually a list of open reviews for this project with some useful info....
		return this.httpClient.get<BackendModelProjectRecentReviews>(url, {});
	}
	
	// TODO: rework this subscription to once.
	getSimpleInformationByProject(projectid:string) : Observable<BackendModelProjectSimpleInformation> {
		var url = `/FutureSQR/rest/project/${projectid}/information`;
		
		return this.httpClient.get<BackendModelProjectSimpleInformation>(url, {});
	}
	
	// TODO: rework this subscription to once.
	createNewReview(projectid:string, revisionid:string): Observable<BackendModelCreateReviewResult> {
		var url = `/FutureSQR/rest/project/${projectid}/review/create`;
		let formdata = new FormData();
		
		formdata.append('revisionid', revisionid);
		
		return this.httpClient.post<BackendModelCreateReviewResult>(url, formdata);
	}
	
	// TODO: rework this subscription to once.
	closeReview(projectid:string, reviewid:string): Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/review/close`;
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	// TODO: rework this subscription to once.
	reopenReview(projectid:string, reviewid:string): Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/review/reopen`;
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	// TODO: rework this subscription to once.
	appendReviewWithRevision(projectid: string, reviewid:string, revisionid:string): Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/appendrevision`;
		
		let formdata = new FormData();
		formdata.append('reviewid',reviewid);
		formdata.append('revisionid',revisionid);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	// TODO: rework this subscription to once.
	deleteReview(projectid:string, reviewid:string): Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/review/delete`;
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	// TODO: rework this subscription to once.
	addReviewer(projectid:string, reviewid:string, reviewerid:string): Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/addreviewer`;
		
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		formdata.append('reviewerid', reviewerid);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	// TODO: rework this subscription to once.
	approveReview(projectid: string, reviewid:string, reviewerid:string) : Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/approvereview`;
		
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		formdata.append('reviewerid', reviewerid);
		
		return this.httpClient.post<any>(url,formdata);
	}

	// TODO: rework this subscription to once.
	concernReview(projectid: string, reviewid:string, reviewerid:string) : Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/concernreview`;
		
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		formdata.append('reviewerid', reviewerid);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	// TODO: rework this subscription to once.
	createThreadForReview(projectid: string, reviewid: string, authorid:string, message:string ) : Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/createthread`;
		
		let formdata = new FormData();
		formdata.append('authorid',authorid);
		formdata.append('message', message);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	// TODO: rework this subscription to once.
	replyThreadMessageForReview(projectid: string, reviewid: string, threadid:string, authorid:string, replytoid:string, message:string) : Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/replythread`;
		
		let formdata = new FormData();
		formdata.append('authorid', authorid);
		formdata.append('threadid', threadid);
		formdata.append('replytoid', replytoid);
		formdata.append('message', message);
		
		return this.httpClient.post<any>(url,formdata);
	}
	
	// TODO: rework this subscription to once.
	updateThreadMessageForReview(projectid: string, reviewid: string, threadid:string, authorid:string, messageid:string, newmessage_text:string) : Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/editmessage`;
		
		let formdata = new FormData();
		formdata.append('authorid', authorid);
		formdata.append('threadid', threadid);
		formdata.append('messageid', messageid);
		formdata.append('newmessage', newmessage_text);
		
		return this.httpClient.post<any>(url,formdata);
	}
}
