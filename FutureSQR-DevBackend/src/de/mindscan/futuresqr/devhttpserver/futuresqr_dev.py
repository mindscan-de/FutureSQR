'''
Created on 05.06.2022

MIT License

Copyright (c) 2022 Maxim Gansert

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

@autor: Maxim Gansert
'''

from fastapi import FastAPI, Form   #, HTTPException

from de.mindscan.futuresqr.gittools.dev_local_git_access import calculateRecentRevisionsForLocalGitRepo, calculateDiffForSingleRevision,\
    calculateFileListForSingleRevision, caluclateSimpleRevisionInformation, calculateRecentRevisionsFromRevisionToHeadForLocalGitRepo,\
    calculateSimpleRevisionInformationForRevisionList, calculateFileListForListOfRevisions, updateProjectCache
from de.mindscan.futuresqr.assets.hardcoded import getProjectConfigurations
from de.mindscan.futuresqr.reviews.review_database import ReviewDatabase
from de.mindscan.futuresqr.projects.project_database import ProjectDatabase
from de.mindscan.futuresqr.reviews.review_tools import createNewReview
from de.mindscan.futuresqr.reviews.review_tables_columns import *  # @UnusedWildImport
from de.mindscan.futuresqr.reviewthreads.review_threads_database import ReviewThreadsDatabase
from de.mindscan.futuresqr.threads.threads_database import ThreadsDatabase


app = FastAPI()

reviewDB = ReviewDatabase({})
projectDB = ProjectDatabase({'allProjects':getProjectConfigurations()})
reviewThreadsDB = ReviewThreadsDatabase({})
threadsDB = ThreadsDatabase({})



@app.get("/")
def read_root():
    return {"message":"Hello World! It works! But now, go away!"}

@app.get("/FutureSQR/rest/user/starredprojects")
def getUserStarredProjects(user_uuid:str = ""):
    result = projectDB.getAllStarredUserProjects(user_uuid)
    return result

@app.get("/FutureSQR/rest/user/allaccessibleprojetcs")
def getUserAllAccessibleProjects(user_uuid: str = ""):
    result = projectDB.getAllUserProjects(user_uuid)
    return result

@app.get("/FutureSQR/rest/project/{projectid}/recentcommits")
def getProjectRevisions(projectid:str):
    if projectDB.hasProjectLocalPath(projectid):
        # TODO: cache this answer for some time and/or limit the number of results?
        revisions = calculateRecentRevisionsForLocalGitRepo(projectDB.getProjectLocalPath(projectid),75)
        # combine revisions with a review list for the revisons and add the revision id to the revision list

        for revision in revisions['revisions']:
            if reviewDB.hasReviewByRevisionId(projectid, revision['revisionid']):
                revision['hasReview']= True
                revision['reviewID']= reviewDB.selectReviewIdByRevisionId(projectid, revision['revisionid'])
            else:
                revision['hasReview']= False
        
        return revisions
    
    # TODO: we should say that this repository is not yet available?        
    result = {'revisions':[]}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/recentcommitsfromrevid/{fromrevisionid}")
def getProjectRevisionsSinceCommitId(projectid: str, fromrevisionid:str):
    if projectDB.hasProjectLocalPath(projectid):
        revisions = calculateRecentRevisionsFromRevisionToHeadForLocalGitRepo(projectDB.getProjectLocalPath(projectid), fromrevisionid)
        # combine revisions with a review list for the revisons and add the revision id to the revision list        

        for revision in revisions['revisions']:
            if reviewDB.hasReviewByRevisionId(projectid, revision['revisionid']):
                revision['hasReview']= True
                revision['reviewID']= reviewDB.selectReviewIdByRevisionId(projectid, revision['revisionid'])
            else:
                revision['hasReview']= False
        
        return revisions

    result = {'revisions':[]}
    return result


@app.get("/FutureSQR/rest/project/{projectid}/information")
def getSimpleProjectInformation(projectid:str):
    if projectDB.isProjectIdPresent(projectid):
        projectInfo = projectDB.getProjectConfiguration(projectid)
        return projectInfo
        
    rseult = {}
    return rseult
    

@app.get("/FutureSQR/rest/project/{projectid}/revisiondiff/{revisionid}")
def getProjectRevisionDiffToPrevious(projectid:str, revisionid:str):
    if projectDB.hasProjectLocalPath(projectid):
        result = calculateDiffForSingleRevision(projectDB.getProjectLocalPath(projectid), revisionid)
        return result
    
    result = {}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/reviewdiff/{reviewid}")
def getProjectReviewDiff(projectid:str, reviewid:str):
    if projectDB.hasProjectLocalPath(projectid):
        reviewData = getReviewData(projectid, reviewid)
        result = calculateDiffForSingleRevision(projectDB.getProjectLocalPath(projectid), reviewData[REVIEW_REVISIONS][0])
        return result
    
    result = {}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/revisionfilelist/{revisionid}")
def getProjectRevisionFileListDiffToPrevious(projectid:str, revisionid:str):
    if projectDB.hasProjectLocalPath(projectid):
        result = calculateFileListForSingleRevision(projectDB.getProjectLocalPath(projectid), revisionid)
        return result
    
    result = {}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/filelist")
def getProjectReviewFileList(projectid:str, reviewid:str):
    if projectDB.hasProjectLocalPath(projectid):
        reviewData = getReviewData(projectid, reviewid)
        result = calculateFileListForListOfRevisions(projectDB.getProjectLocalPath(projectid), reviewData[REVIEW_REVISIONS])
        return result
    
    result = {}
    return result


@app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/information")
def getReviewData(projectid:str, reviewid:str):
    if projectDB.isProjectIdPresent(projectid):
        return reviewDB.selectReviewByReviewId(projectid, reviewid);
    
    result = {}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/revisiondetails")
def getReviewRevisionInformation(projectid:str, reviewid:str):
    if projectDB.isProjectIdPresent(projectid):
        # get revison numbers from review
        revisions=reviewDB.selectRevisionsForReview(projectid, reviewid)
        # get revision information from
        revisionDetails = calculateSimpleRevisionInformationForRevisionList(projectDB.getProjectLocalPath(projectid),revisions)
        return revisionDetails
    
    result = []
    return result


@app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/threads")
def getReviewThreadsInformation(projectid:str, reviewid:str):
    threadlist = reviewThreadsDB.selectThreadsForReview(projectid, reviewid)
    if len(threadlist) is 0:
        return {'allreviewthreads':[]}
    
    result = [ threadsDB.selectFullThread(thread_uuid) for thread_uuid in threadlist]
    return {'allreviewthreads': result} 
    
    
@app.get("/FutureSQR/rest/project/{projectid}/recentreviews")    
def getRecentReviews(projectid:str):
    if projectDB.isProjectIdPresent(projectid):
        result = {
            'openReviews':reviewDB.selectOpenReviewsByProjectId(projectid),
            'recentClosedReviews':[]
                  }
        return result
    
    rseult = {}
    return rseult

@app.get("/FutureSQR/rest/project/{projectid}/revision/{revisionid}/information")
def getSimpleReviewInfomation(projectid:str, revisionid:str):
    if projectDB.hasProjectLocalPath(projectid):
        revinfo = caluclateSimpleRevisionInformation(projectDB.getProjectLocalPath(projectid), revisionid)
        
        for revision in revinfo:
            if reviewDB.hasReviewByRevisionId(projectid, revisionid):
                revision['hasReview']= True
                revision['reviewID']= reviewDB.selectReviewIdByRevisionId(projectid, revisionid)
            else:
                revision['hasReview']= False
        
        #TODO: we might have to calculate the review.
        return revinfo[0]
    
    rseult = {}
    return rseult

### #########################################
###
### Some Project functions - non persistent
###
### #########################################
        
@app.post("/FutureSQR/rest/project/{projectid}/star")
def postStarProjectForUser(projectid:str, userid:str=''):
    if not projectDB.isProjectIdPresent(projectid):
        return {} 
    
    # this will star a project
    # actually this should be done on a ProjectUserRelation, but for now we don't have separate users
    projectDB.starProject(projectid)
    
    return {}

@app.post("/FutureSQR/rest/project/{projectid}/unstar")
def postUnstarProjectForUser(projectid:str, userid:str=''):
    if not projectDB.isProjectIdPresent(projectid):
        return {} 
    
    # this will remove a star set for a project.
    # actually this should be done on a ProjectUserRelation, but for now we don't have separate users
    projectDB.unstarProject(projectid)
    
    return {}


### #################################################
###
### Some Disucssion thread functions - non persistent
###
### #################################################

@app.post("/FutureSQR/rest/project/{projectid}/review/{reviewid}/createthread")
def postCreateNewReviewThread(projectid:str, reviewid:str, authorid:str = Form(...), message:str =Form(...) ):
    # todo, we need some privileges check here in future....
    newthreaduuid=threadsDB.createNewThread(message, authorid)
    reviewThreadsDB.addThreadToReview(projectid, reviewid, newthreaduuid)
    return {}

#app.post("/FutureSQR/rest/project/{projectid}/review/{reviewid}/replythread")
def postReplyToReviewThread(projectid:str, reviewid:str, threadid:str =Form(...), replytoid:str = Form(...), authorid:str = Form(...), message:str =Form(...) ):
    # we actually don't need project id and reviewid here, but we need this for privileges check,
    # and also checks that the id's all exists properly
    messageuuid = threadsDB.createMessageResponse(threadid, replytoid, message, authorid)
    return {}

### #########################################
###
### Some Review functions - non persistent
###
### #########################################

# to create a new review from a revision
@app.post("/FutureSQR/rest/project/{projectid}/review/create")
def postCreateNewReview(projectid:str, revisionid:str = Form(...)):
    if not projectDB.isProjectIdPresent(projectid):
        return {} 

    # TODO: check if the review revisionid is already covered by a review
    if reviewDB.hasReviewByRevisionId(projectid, revisionid):
        # 'projectid':projectid,
        # 'revisionid':revisionid,
        # 'reviewid':knownReview[REVIEW_PK_REVIEW_ID],
        # 'reviewdata':knownReview
        return {}


    # * get someinformation about this particular version heading and so on for the review title
    shortrevInfo=getSimpleReviewInfomation(projectid, revisionid)
    
    revisionInformation = {
            'firstCommitLine': shortrevInfo['message'],
            'revisionID':shortrevInfo['revisionid'],
            'author':shortrevInfo['authorname']
        }
    
    
    # * we then create a new review in the backend
    #   * we get then a new unique review ID back
    newReview = createNewReview(projectDB, projectid, revisionInformation)
    
    if newReview is None:
        return {}
    
    reviewDB.insertReview(projectid, newReview)
    
    result = {
            'projectId':projectid,
            'revisionId':revisionid,
            'reviewId':newReview[REVIEW_PK_REVIEW_ID],
            'reviewData':newReview
        }
    return result
    
    
    
@app.post("/FutureSQR/rest/project/{projectid}/review/close")
def postCloseReview(projectid:str, reviewid:str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.updateCloseReviewByReviewId(projectid, reviewid)
    
    result = {}
    return result
    
    
@app.post("/FutureSQR/rest/project/{projectid}/review/reopen")
def postReopenReview(projectid:str, reviewid:str=Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.updateReopenReviewByReviewId(projectid, reviewid)
    
    result = {}
    return result
    

@app.post("/FutureSQR/rest/project/{projectid}/review/delete")
def postDeleteReview(projectid:str, reviewid:str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.updateDeleteReviewByReviewId(projectid, reviewid)
    
    result = {}
    return result

@app.post("/FutureSQR/rest/project/{projectid}/review/addreviewer")
def postAddReviewerToReview(projectid:str, reviewid:str = Form(...), reviewerid:str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.insertReviewerToReview(projectid, reviewid, reviewerid)
            
    result = {}
    return result

@app.post("/FutureSQR/rest/project/{projectid}/review/approvereview")
def postReviewApprove(projectid:str, reviewid:str = Form(...), reviewerid:str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.approveReview(projectid, reviewid, reviewerid)
    result = {}
    return result

@app.post("/FutureSQR/rest/project/{projectid}/review/concernreview")
def postReviewConcern(projectid:str, reviewid:str = Form(...), reviewerid:str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.concernReview(projectid, reviewid, reviewerid)
    result = {}
    return result

@app.post("/FutureSQR/rest/project/{projectid}/review/appendrevision")
def postAppendRevisionToReview(projectid:str, reviewid:str = Form(...), revisionid:str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        # get all revisions since first revision no previous revision can be addded 
        # filter all revisions whether it is bound to a review... 
        # filter all revisions and use this as order, check if tzhe revision is pat of this list
        # TODO: 
        # calculate the correct order of the added revision (not that easy for GIT)....
        # add in correct order, because the hashes have an implicit order, by the parent relationship.
        reviewDB.addRevisionToReview(projectid, reviewid, revisionid)
    result = {}
    return result

@app.post("/FutureSQR/rest/project/{projectid}/updatecache")
def postUpdateProjectCache(projectid: str):
    if projectDB.hasProjectLocalPath(projectid):
        project_branch_name = projectDB.getProjectBranchName(projectid)
        if project_branch_name is not None:
            project_path = projectDB.getProjectLocalPath(projectid)
            updateProjectCache(project_path, project_branch_name)
    result = {}
    return result

### #########################################
###
### Review Threads
###
### #########################################

def getReviewThreads(projectid: str, reviewid:str):
    pass

def postCreateReviewThread():
    pass