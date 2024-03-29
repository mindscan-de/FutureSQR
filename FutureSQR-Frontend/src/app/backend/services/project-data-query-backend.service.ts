import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { map, first } from 'rxjs/operators';

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
import { BackendModelRevisionFileContent } from '../model/backend-model-revision-file-content';
import { BackendModelSingleFileCommitHistory } from '../model/backend-model-single-file-commit-history';

// ---- user type backend data
import { BackendModelSimpleUserDictionary } from '../model/backend-model-simple-user-dictionary';


/**
 * TODO: rework all subscription and post events to avoid any memory and/or resource leaks.
 */

@Injectable({
  providedIn: 'root'
})
export class ProjectDataQueryBackendService {
	
	private static readonly URL_GET_ALL_PROJECTS:string           = "/FutureSQR/rest/user/allaccessibleprojects";
	private static readonly URL_GET_MY_STARRED_PROJECTS:string    = "/FutureSQR/rest/user/starredprojects";
	// TODO: this should be cooler
	private static readonly URL_GET_RECENT_PROJECT_COMMITS:string = "/FutureSQR/rest/project/${projectid}/recentcommits";
	private static readonly URL_GET_PROJECT_REVISION_DIFF:string  = "/FutureSQR/rest/project/${projectid}/revisiondiff/${revisionid}";
	private static readonly URL_GET_PROJECT_REVISION_FILES:string = "/FutureSQR/rest/project/${projectid}/filelist/${revisionid}";

    constructor(
		private httpClient : HttpClient 
	) { }

    getAllProjects (userid:string) : Observable<BackendModelProjectItem[]> {
		let params = new HttpParams().set('userid',userid);
	
	    return this.httpClient
				.get<BackendModelProjectItem[]>(ProjectDataQueryBackendService.URL_GET_ALL_PROJECTS, { params: params })
				.pipe(first());
    }

	getStarredProjects(userid:string) : Observable<BackendModelProjectItem[]> {
		let params = new HttpParams().set('userid',userid);
		
		return this.httpClient
				.get<BackendModelProjectItem[]>(ProjectDataQueryBackendService.URL_GET_MY_STARRED_PROJECTS, { params: params })
				.pipe(first());
	}
	
	starProject(projectid:string, currentuser_uuid:string) : Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/star`;
		let formdata = new FormData();
		
		formdata.append('userid', currentuser_uuid);
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	unstarProject(projectid:string, currentuser_uuid:string) : Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/unstar`;
		let formdata = new FormData();
		
		formdata.append('userid' ,currentuser_uuid);
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	updateProjectCache(projectid:string) : Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/updatecache`;
		let formdata = new FormData();
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	getRecentProjectCommits(projectid:string) : Observable<BackendModelProjectRecentCommits> {
		var url = `/FutureSQR/rest/project/${projectid}/recentcommits`;
		
		return this.httpClient
				.get<BackendModelProjectRecentCommits>(url, {})
				.pipe(first());
	}
	
	getRecentProjectCommitsSinceRevision(projectid: string, revisionid:string):Observable<BackendModelProjectRecentCommits> {
		var url = `/FutureSQR/rest/project/${projectid}/recentcommitsfromrevid/${revisionid}`;
		
		return this.httpClient
				.get<BackendModelProjectRecentCommits>(url, {})
				.pipe(first());
	} 	
	
	getRecentProjectRevisionDiffFullChangeSet(projectid:string, revisionid:string): Observable<BackendModelSingleCommitFullChangeSet> {
		var url = `/FutureSQR/rest/project/${projectid}/revisiondiff/${revisionid}`;
		
		return this.httpClient
				.get<BackendModelSingleCommitFullChangeSet>(url, {})
				.pipe(first());
	}
	
	getParticularFileRevisionContent(projectid:string, revisionid:string, filepath:string): Observable<BackendModelRevisionFileContent> {
		var url = `/FutureSQR/rest/project/${projectid}/revisionfilecontent/${revisionid}`;
		
		let params = new HttpParams().set('filepath',filepath);
		
		return this.httpClient
				.get<BackendModelRevisionFileContent>(url, { params: params })
				.pipe(first());
	}
	
	getParticularFileCommitHistory(projectid:string, filepath:string): Observable<BackendModelSingleFileCommitHistory> {
		var url = `/FutureSQR/rest/project/${projectid}/filehistory`;
		let params = new HttpParams().set('filepath',filepath);
		
		return this.httpClient
				.get<BackendModelSingleFileCommitHistory>(url, { params: params })
				.pipe(first());
	}
	
	getReviewRevisionDiffFullChangeSet(projectid:string, reviewid:string, activatedRevisions:string="") : Observable<BackendModelSingleCommitFullChangeSet> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/diff`;
		
		let params = new HttpParams().set('selected',activatedRevisions);
		
		return this.httpClient
				.get<BackendModelSingleCommitFullChangeSet>(url, { params: params })
				.pipe(first());
	}
	
	getRecentProjectRevisionFilePathsData(projectid:string, revisionid:string): Observable<BackendModelSingleCommitFileActionsInfo> {
		var url = `/FutureSQR/rest/project/${projectid}/revisionfilelist/${revisionid}`;
		
		return this.httpClient
				.get<BackendModelSingleCommitFileActionsInfo>(url, {})
				.pipe(first());
	}
	
	getRecentProjectRevisionInformation(projectid:string, revisionid:string): Observable<BackendModelProjectRecentCommitRevision> {
		var url = `/FutureSQR/rest/project/${projectid}/revision/${revisionid}/information`;
		
		return this.httpClient
				.get<BackendModelProjectRecentCommitRevision>(url, {})
				.pipe(first());
	}
	
	getReviewFilePathsData(projectid:string, reviewid:string): Observable<BackendModelSingleCommitFileActionsInfo> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/filelist`;
		
		return this.httpClient
				.get<BackendModelSingleCommitFileActionsInfo>(url, {})
				.pipe(first());
	}
	
	getReviewData(projectid:string, reviewid:string): Observable<BackendModelReviewData> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/information`;
		
		return this.httpClient
				.get<BackendModelReviewData>(url, {})
				.pipe(first());
	}
	
	getThreadData(projectid:string, reviewid:string): Observable<BackendModelThreadsData> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/threads`;
		
		return this.httpClient
				.get<BackendModelThreadsData>(url, {})
				.pipe(first());
	}
	
	getReviewSimpleRevisionInformationList(projectid: string, reviewid: string): Observable <BackendModelProjectRecentCommitRevision[]> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/revisiondetails`
		
		return this.httpClient
				.get<BackendModelProjectRecentCommitRevision[]>(url, {})
				.pipe(first());
	}
	
	getRecentReviewsByProject(projectid:string): Observable<BackendModelProjectRecentReviews> {
		var url = `/FutureSQR/rest/project/${projectid}/recentreviews`;
		
		// returns actually a list of open reviews for this project with some useful info....
		return this.httpClient
				.get<BackendModelProjectRecentReviews>(url, {})
				.pipe(first());
	}
	
	getSimpleInformationByProject(projectid:string) : Observable<BackendModelProjectSimpleInformation> {
		var url = `/FutureSQR/rest/project/${projectid}/information`;
		
		return this.httpClient
				.get<BackendModelProjectSimpleInformation>(url, {})
				.pipe(first());
	}
	
	createNewReview(projectid:string, revisionid:string, opening_userid:string): Observable<BackendModelCreateReviewResult> {
		var url = `/FutureSQR/rest/project/${projectid}/review/create`;
		let formdata = new FormData();
		
		formdata.append('revisionid', revisionid);
		formdata.append('opening_userid', opening_userid);
		
		return this.httpClient
				.post<BackendModelCreateReviewResult>(url, formdata)
				.pipe(first());
	}
	
	closeReview(projectid:string, reviewid:string, closing_userid:string): Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/review/close`;
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		formdata.append('closing_userid', closing_userid);
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	reopenReview(projectid:string, reviewid:string, reopening_userid:string): Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/review/reopen`;
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		formdata.append('reopening_userid', reopening_userid);
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	appendRevisionToReview(projectid: string, reviewid:string, revisionid:string, userid:string): Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/appendrevision`;
		
		let formdata = new FormData();
		formdata.append('reviewid',reviewid);
		formdata.append('revisionid',revisionid);
		formdata.append('userid', userid);
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	removeRevisionFromReview(projectid: string, reviewid: string, revisionid: string, userid: string): Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/removerevision`;

		let formdata = new FormData();
		formdata.append('reviewid',reviewid);
		formdata.append('revisionid',revisionid);
		formdata.append('userid', userid);

		return this.httpClient
				.post<any>(url, formdata)
				.pipe(first());		
	}
	
	deleteReview(projectid:string, reviewid:string): Observable<any> {
		var url =`/FutureSQR/rest/project/${projectid}/review/delete`;
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	addReviewer(projectid:string, reviewid:string, reviewerid:string, userid:string): Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/addreviewer`;
		
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		formdata.append('reviewerid', reviewerid);
		// the user who added the reviewer (might be different from the reviewer.)
		formdata.append('userid',userid);
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	removeReviewer(projectid:string, reviewid:string, reviewerid:string, userid:string): Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/removereviewer`;
	
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		formdata.append('reviewerid', reviewerid);
		// the user who removed the reviewer (might be different from the reviewer.)
		formdata.append('userid',userid);
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	getSuggestedReviewers(projectid:string, reviewid:string ): Observable<BackendModelSimpleUserDictionary> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/suggestedreviewers`;
		return this.httpClient
				.get<BackendModelSimpleUserDictionary>(url,{})
				.pipe(first());
	}
	
	approveReview(projectid: string, reviewid:string, reviewerid:string) : Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/approvereview`;
		
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		formdata.append('reviewerid', reviewerid);
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}

	concernReview(projectid: string, reviewid:string, reviewerid:string) : Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/concernreview`;
		
		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		formdata.append('reviewerid', reviewerid);
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	resetReview(projectid: string, reviewid:string, reviewerid:string) : Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/retractreview`;

		let formdata = new FormData();
		
		formdata.append('reviewid',reviewid);
		formdata.append('reviewerid', reviewerid);

		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	createThreadForReview(projectid: string, reviewid: string, authorid:string, message:string ) : Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/createthread`;
		
		let formdata = new FormData();
		formdata.append('authorid',authorid);
		formdata.append('message', message);
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	replyThreadMessageForReview(projectid: string, reviewid: string, threadid:string, authorid:string, replytoid:string, message:string) : Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/replythread`;
		
		let formdata = new FormData();
		formdata.append('authorid', authorid);
		formdata.append('threadid', threadid);
		formdata.append('replytoid', replytoid);
		formdata.append('message', message);
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	updateThreadMessageForReview(projectid: string, reviewid: string, threadid:string, authorid:string, messageid:string, newmessage_text:string) : Observable<any> {
		var url = `/FutureSQR/rest/project/${projectid}/review/${reviewid}/editmessage`;
		
		let formdata = new FormData();
		formdata.append('authorid', authorid);
		formdata.append('threadid', threadid);
		formdata.append('messageid', messageid);
		formdata.append('newmessage', newmessage_text);
		
		return this.httpClient
				.post<any>(url,formdata)
				.pipe(first());
	}
	
	
}
