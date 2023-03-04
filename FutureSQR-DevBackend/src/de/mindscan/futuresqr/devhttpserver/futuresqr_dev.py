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

from fastapi import FastAPI, Form, HTTPException, status
from fastapi.responses import JSONResponse ## TODO HTMLResponse

from de.mindscan.futuresqr.scmtools.git.dev_local_git_access import calculateRecentRevisionsForLocalGitRepo, calculateDiffForSingleRevision,\
    calculateFileListForSingleRevision, calculateSimpleRevisionInformation, calculateRecentRevisionsFromRevisionToHeadForLocalGitRepo,\
    calculateSimpleRevisionInformationForRevisionList, calculateFileListForListOfRevisions, updateProjectCache,\
    getParticularRevisionContentForFile, getParticularFileHistory
from de.mindscan.futuresqr.reviews.review_database import ReviewDatabase
from de.mindscan.futuresqr.projects.project_database import ProjectDatabase
from de.mindscan.futuresqr.reviews.review_tools import createNewReview
from de.mindscan.futuresqr.reviews.review_tables_columns import *  # @UnusedWildImport
from de.mindscan.futuresqr.reviewthreads.review_threads_database import ReviewThreadsDatabase
from de.mindscan.futuresqr.threads.threads_database import ThreadsDatabase
from de.mindscan.futuresqr.users.users_database import UsersDatabase
from de.mindscan.futuresqr.assets.hardcoded import getSystemConfigurationMap
from de.mindscan.futuresqr.configuration.system_configuration import SystemConfiguration
from de.mindscan.futuresqr.authnsession.session_database import SessionDatabase
from de.mindscan.futuresqr.users.user_table_columns import *  # @UnusedWildImport

import de.mindscan.futuresqr.devhttpserver.myconfig as myconfig

### ---------------------------------------------------------------
### provide general system configuration - for different developers
### ---------------------------------------------------------------

systemConfiguration = SystemConfiguration({'SystemConfigMap':getSystemConfigurationMap(myconfig.MY_CONFIGNAME)})

### --------------------------
### Initialize the Applictaion
### --------------------------

app = FastAPI()

reviewDB = ReviewDatabase({})
projectDB = ProjectDatabase({
    'allProjects':{},
    'persistenceActive':False
    })
reviewThreadsDB = ReviewThreadsDatabase({})
threadsDB = ThreadsDatabase({})
usersDB = UsersDatabase({
    'persistenceActive': False
    });
sessionDB = SessionDatabase({})

### --------------
### REST Endpoints
### --------------

@app.get("/", response_class=JSONResponse)
def read_root():
    return {"message":"Hello World! It works! But now, go away!"}

@app.get("/FutureSQR/rest/user/starredprojects", response_class=JSONResponse)
def getUserStarredProjects(user_uuid:str = ""):
    # Either get the user_UUID from the session or from the URL
    # if from the  URL, does it need protection, eg validation against current user in session?
    
    result = projectDB.getAllStarredUserProjects(user_uuid)
    return result

@app.get("/FutureSQR/rest/user/allaccessibleprojects", response_class=JSONResponse)
def getUserAllAccessibleProjects(user_uuid: str = ""):
    # this userid needs to be checked. the user id mus come from the authorized session
    # for some time it would be acceptable to hava a get url parameter for this, but for
    # security reasons this must be then nailed to the session info.
    result = projectDB.getAllUserProjects(user_uuid)
    return result

@app.get("/FutureSQR/rest/project/{projectid}/recentcommits", response_class=JSONResponse)
def getProjectRevisions(projectid:str):
    if projectDB.hasProjectLocalPath(projectid):
        # TODO: cache this answer for some time and/or limit the number of results?
        fullScmPath = systemConfiguration.calculateScmCacheFolder(projectDB.getProjectLocalPath(projectid))
        revisions = calculateRecentRevisionsForLocalGitRepo(fullScmPath,75)
        # combine revisions with a review list for the revisons and add the revision id to the revision list

        for revision in revisions['revisions']:
            if reviewDB.hasReviewByRevisionId(projectid, revision['revisionid']):
                revision['hasReview']= True
                review = reviewDB.selectReviewByRevisionId(projectid, revision['revisionid'])
                revision['reviewID'] = review[REVIEW_REVIEW_ID]
                revision['reviewClosed'] = review[REVIEW_LIFECYLCE_STATE] == REVIEW_LIFECYCLE_STATE_CLOSED
            else:
                revision['hasReview']= False
        
        return revisions
    
    # TODO: we should say that this repository is not yet available?        
    result = {'revisions':[]}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/recentcommitsfromrevid/{fromrevisionid}", response_class=JSONResponse)
def getProjectRevisionsSinceCommitId(projectid: str, fromrevisionid:str):
    if projectDB.hasProjectLocalPath(projectid):
        fullScmPath = systemConfiguration.calculateScmCacheFolder(projectDB.getProjectLocalPath(projectid))        
        revisions = calculateRecentRevisionsFromRevisionToHeadForLocalGitRepo(fullScmPath, fromrevisionid)
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


@app.get("/FutureSQR/rest/project/{projectid}/information", response_class=JSONResponse)
def getSimpleProjectInformation(projectid:str):
    if projectDB.isProjectIdPresent(projectid):
        projectInfo = projectDB.getProjectConfiguration(projectid)
        return projectInfo
        
    rseult = {}
    return rseult
    

@app.get("/FutureSQR/rest/project/{projectid}/revisiondiff/{revisionid}", response_class=JSONResponse)
def getProjectRevisionDiffToPrevious(projectid:str, revisionid:str):
    if projectDB.hasProjectLocalPath(projectid):
        fullScmPath = systemConfiguration.calculateScmCacheFolder(projectDB.getProjectLocalPath(projectid))
        result = calculateDiffForSingleRevision(fullScmPath, revisionid)
        return result
    
    result = {}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/revisionfilecontent/{revisionid}", response_class=JSONResponse)
def getParticularFileRevisionContent(projectid:str, revisionid:str, filepath:str):
    if(projectDB.hasProjectLocalPath(projectid)):
        fullScmPath = systemConfiguration.calculateScmCacheFolder(projectDB.getProjectLocalPath(projectid))
        result = getParticularRevisionContentForFile(fullScmPath, revisionid, filepath)
        return result;
    
    result = {}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/filehistory", response_class=JSONResponse)
def getParticularRevisionsForFile(projectid:str, filepath:str):
    if(projectDB.hasProjectLocalPath(projectid)):
        fullScmPath = systemConfiguration.calculateScmCacheFolder(projectDB.getProjectLocalPath(projectid))
        result = getParticularFileHistory(fullScmPath, filepath)
        return result;
    
    result = {}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/diff", response_class=JSONResponse)
def getProjectReviewDiff(projectid:str, reviewid:str):
    if projectDB.hasProjectLocalPath(projectid):
        reviewData = getReviewData(projectid, reviewid)
        fullScmPath = systemConfiguration.calculateScmCacheFolder(projectDB.getProjectLocalPath(projectid))
        result = calculateDiffForSingleRevision(fullScmPath, reviewData[REVIEW_REVISIONS][0])
        return result
    
    result = {}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/revisionfilelist/{revisionid}", response_class=JSONResponse)
def getProjectRevisionFileListDiffToPrevious(projectid:str, revisionid:str):
    if projectDB.hasProjectLocalPath(projectid):
        fullScmPath = systemConfiguration.calculateScmCacheFolder(projectDB.getProjectLocalPath(projectid))
        result = calculateFileListForSingleRevision(fullScmPath, revisionid)
        return result
    
    result = {}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/filelist", response_class=JSONResponse)
def getProjectReviewFileList(projectid:str, reviewid:str):
    if projectDB.hasProjectLocalPath(projectid):
        reviewData = getReviewData(projectid, reviewid)
        fullScmPath = systemConfiguration.calculateScmCacheFolder(projectDB.getProjectLocalPath(projectid))
        result = calculateFileListForListOfRevisions(fullScmPath, reviewData[REVIEW_REVISIONS])
        return result
    
    result = {}
    return result


@app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/information", response_class=JSONResponse)
def getReviewData(projectid:str, reviewid:str):
    if projectDB.isProjectIdPresent(projectid):
        return reviewDB.selectReviewByReviewId(projectid, reviewid);
    
    result = {}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/revisiondetails", response_class=JSONResponse)
def getReviewRevisionInformation(projectid:str, reviewid:str):
    if projectDB.isProjectIdPresent(projectid):
        # get revison numbers from review
        revisions=reviewDB.selectRevisionsForReview(projectid, reviewid)
        # get revision information from
        fullScmPath = systemConfiguration.calculateScmCacheFolder(projectDB.getProjectLocalPath(projectid))
        revisionDetails = calculateSimpleRevisionInformationForRevisionList(fullScmPath,revisions)
        return revisionDetails
    
    result = []
    return result

@app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/suggestedreviewers", response_class=JSONResponse)
def getSuggestedReviewersForReview(projectid:str, reviewid:str):
    # TODO query which user might be useful for a given projectid + reviewid
    # We want to return the uuid's of the users, or we might already resolve them / which makes things in the frontend easier.
    # maybe a map of uuids, with simple resolved names.
    
    # provide two hardcoded suggested users right now.
    # TODO: implement me better!
    suggestedreviewers = ["8ce74ee9-48ff-3dde-b678-58a632887e31", "f5fc8449-3049-3498-9f6b-ce828515bba2"]


    allusers = usersDB.selectAllUSers()
    simpleUserMap = usersDB.getAsSimpleUserMap(allusers)
    reviewermap = { uuid : simpleUserMap[uuid] for uuid in suggestedreviewers }

    # good enough?    
    return { 'dictionary' : reviewermap }
    



@app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/threads", response_class=JSONResponse)
def getReviewThreadsInformation(projectid:str, reviewid:str):
    threadlist = reviewThreadsDB.selectThreadsForReview(projectid, reviewid)
    if len(threadlist) is 0:
        return {'allreviewthreads':[]}
    
    result = [ threadsDB.selectFullThread(thread_uuid) for thread_uuid in threadlist]
    return {'allreviewthreads': result} 
    
    
@app.get("/FutureSQR/rest/project/{projectid}/recentreviews", response_class=JSONResponse)    
def getRecentReviews(projectid:str):
    if projectDB.isProjectIdPresent(projectid):
        result = {
            'openReviews':reviewDB.selectOpenReviewsByProjectId(projectid),
            'recentClosedReviews':reviewDB.selectClosedReviewsByProjectId(projectid)
                  }
        return result
    
    rseult = {}
    return rseult

@app.get("/FutureSQR/rest/project/{projectid}/revision/{revisionid}/information", response_class=JSONResponse)
def getSimpleReviewInfomation(projectid:str, revisionid:str):
    if projectDB.hasProjectLocalPath(projectid):
        fullScmPath = systemConfiguration.calculateScmCacheFolder(projectDB.getProjectLocalPath(projectid))
        revinfo = calculateSimpleRevisionInformation(fullScmPath, revisionid)
        
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
        
@app.post("/FutureSQR/rest/project/{projectid}/star", response_class=JSONResponse)
def postStarProjectForUser(projectid:str, userid:str=Form(...)):
    if not projectDB.isProjectIdPresent(projectid):
        return {} 
    
    # this will star a project
    # actually this should be done on a ProjectUserRelation, but for now we don't have separate users
    projectDB.starProject(projectid)
    
    return {}

@app.post("/FutureSQR/rest/project/{projectid}/unstar", response_class=JSONResponse)
def postUnstarProjectForUser(projectid:str, userid:str=Form(...)):
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

@app.post("/FutureSQR/rest/project/{projectid}/review/{reviewid}/createthread", response_class=JSONResponse)
def postCreateNewReviewThread(projectid:str, reviewid:str, authorid:str = Form(...), message:str =Form(...) ):
    # todo, we need some privileges check here in future....
    newthreaduuid=threadsDB.createNewThread(message, authorid)
    reviewThreadsDB.addThreadToReview(projectid, reviewid, newthreaduuid)
    return {}

@app.post("/FutureSQR/rest/project/{projectid}/review/{reviewid}/replythread", response_class=JSONResponse)
def postReplyToReviewThread(projectid:str, reviewid:str, threadid:str =Form(...), replytoid:str = Form(...), authorid:str = Form(...), message:str =Form(...) ):
    # we actually don't need project id and reviewid here, but we need this for privileges check,
    # and also checks that the id's all exists properly
    messageuuid = threadsDB.createMessageResponse(threadid, replytoid, message, authorid)
    return {}

@app.post("/FutureSQR/rest/project/{projectid}/review/{reviewid}/editmessage", response_class=JSONResponse)
def updateThreadMessage(projectid:str, reviewid:str, threadid:str=Form(...), messageid:str = Form(...), authorid:str = Form(...), newmessage:str=Form(...)):
    # we want the project id and the review id, threadid here for privilege checks
    # also we want to make sure that the projectid the reviewid and the threadid, authorid is checked before the update
    threadsDB.updateMessage(messageid, newmessage)
    return {}

### #########################################
###
### Some Review functions - non persistent
###
### #########################################

# to create a new review from a revision
@app.post("/FutureSQR/rest/project/{projectid}/review/create", response_class=JSONResponse)
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
    
    
    
@app.post("/FutureSQR/rest/project/{projectid}/review/close", response_class=JSONResponse)
def postCloseReview(projectid:str, reviewid:str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.updateCloseReviewByReviewId(projectid, reviewid)
    
    result = {}
    return result
    
    
@app.post("/FutureSQR/rest/project/{projectid}/review/reopen", response_class=JSONResponse)
def postReopenReview(projectid:str, reviewid:str=Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.updateReopenReviewByReviewId(projectid, reviewid)
    
    result = {}
    return result
    

@app.post("/FutureSQR/rest/project/{projectid}/review/delete", response_class=JSONResponse)
def postDeleteReview(projectid:str, reviewid:str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.updateDeleteReviewByReviewId(projectid, reviewid)
    
    result = {}
    return result


@app.post("/FutureSQR/rest/project/{projectid}/review/addreviewer", response_class=JSONResponse)
def postAddReviewerToReview(projectid:str, reviewid:str = Form(...), reviewerid:str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.insertReviewerToReview(projectid, reviewid, reviewerid)
            
    result = {}
    return result


@app.post("/FutureSQR/rest/project/{projectid}/review/removereviewer", response_class=JSONResponse)
def postRemoveReviewerFromReview(projectid:str, reviewid:str = Form(...), reviewerid:str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.deleteReviewerFromReview(projectid, reviewid, reviewerid)
            
    result = {}
    return result
    

@app.post("/FutureSQR/rest/project/{projectid}/review/approvereview", response_class=JSONResponse)
def postReviewApprove(projectid:str, reviewid:str = Form(...), reviewerid:str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.approveReview(projectid, reviewid, reviewerid)
    result = {}
    return result

@app.post("/FutureSQR/rest/project/{projectid}/review/concernreview", response_class=JSONResponse)
def postReviewConcern(projectid:str, reviewid:str = Form(...), reviewerid:str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.concernReview(projectid, reviewid, reviewerid)
    result = {}
    return result

@app.post("/FutureSQR/rest/project/{projectid}/review/retractreview", response_class=JSONResponse)
def postReviewReset( projectid:str, reviewid:str=Form(...), reviewerid:str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        # reviewDB.resetReview(projectid, reviewid, reviewerid)
        pass
    result = {}
    return result
    

@app.post("/FutureSQR/rest/project/{projectid}/review/appendrevision", response_class=JSONResponse)
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

@app.post("/FutureSQR/rest/project/{projectid}/review/removerevision", response_class=JSONResponse)
def postRemoveRevisionFromReviw(projectid:str, reviewid:str = Form(...), revisionid: str = Form(...)):
    if projectDB.isProjectIdPresent(projectid):
        reviewDB.removeRevisionFromReview(projectid, reviewid, revisionid)
    result = {}
    return result

@app.post("/FutureSQR/rest/project/{projectid}/updatecache", response_class=JSONResponse)
def postUpdateProjectCache(projectid: str):
    if projectDB.hasProjectLocalPath(projectid):
        project_branch_name = projectDB.getProjectBranchName(projectid)
        if project_branch_name is not None:
            fullScmPath = systemConfiguration.calculateScmCacheFolder(projectDB.getProjectLocalPath(projectid))
            updateProjectCache(fullScmPath, project_branch_name)
    result = {}
    return result

### #########################################
###
### Some more Review Stuff
###
### #########################################


### #########################################
###
### Some User Data Stuff
###
### #########################################

@app.get("/FutureSQR/rest/user/userdictionary", response_class=JSONResponse)
def getSimpleUserDictionary():
    # this one should be reduced to a map
    # key (uuid) -> array of [uuid, displayname, avatarlocation, isbanned]
    # this one is loaded by every user to resolve the uuids
    allusers = usersDB.selectAllUSers()
    simpleUserMap = usersDB.getAsSimpleUserMap(allusers)
    
    return { 'dictionary' : simpleUserMap }


### #########################################
###
### login / logout / Whoami Stuff
### single authenticated entity....
###
### #########################################

@app.post("/FutureSQR/rest/user/authenticate", response_class=JSONResponse)
def postLoginData(
        username:str = Form(...), 
        password:str = Form(...)):

    # find user by uername in user passwd database
    # because we don't have a database, we use the user database, and check for presence 
    #     pwEntry = getPasswdEntry(username)
    #     if pwEntry is None:
    isUserPresent = usersDB.hasUserByLogonNme(username)
    if not isUserPresent:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="No such user or not authenticated"
            )
    # find userentry by username in user database
    userEntry = usersDB.getUserByLogonName(username);
    # check if user is banned
    
    # verify password,
    # either we generate a valid user dataset response
    # or we handle the wrong password by how?
    
    sessionDB.userAuthenticationRegister(userEntry)
    
    capabilities = {}
    capabilities['roles'] = []
    capabilities['featureflags'] = {}
    
    if username=='mindscan-de':
        capabilities['roles'].append('admin')
    
    return {
        USER_UUID:userEntry[USER_UUID],
        USER_LOGON_NAME:userEntry[USER_LOGON_NAME],
        USER_DISPLAYNAME:userEntry[USER_DISPLAYNAME],
        USER_AVATARLOCATION:userEntry[USER_AVATARLOCATION],
        USER_CONTACT_EMAIL:userEntry[USER_CONTACT_EMAIL],
        'capabilities': capabilities,
        }

@app.post("/FutureSQR/rest/user/reauthenticate", response_class=JSONResponse)
def postReauthenticateLoginData(
            assumedusername: str = Form(...)
        ):
    
    if sessionDB.isAuthenticationPresent(assumedusername):
        userEntry = usersDB.getUserByLogonName(assumedusername);
        
        capabilities = {}
        capabilities['roles'] = []
        capabilities['featureflags'] = {}
        
        if assumedusername=='mindscan-de':
            capabilities['roles'].append('admin')
            
        return {
            USER_UUID:userEntry[USER_UUID],
            USER_LOGON_NAME:userEntry[USER_LOGON_NAME],
            USER_DISPLAYNAME:userEntry[USER_DISPLAYNAME],
            USER_AVATARLOCATION:userEntry[USER_AVATARLOCATION],
            USER_CONTACT_EMAIL:userEntry[USER_CONTACT_EMAIL],
            'capabilities': capabilities,            
            }
    
    # TODO: check if things match - in real backend this is more complicated.
    # we do someting very wild, we compare that with the username, 
    # who logged on last. dev-backend will only support one single
    # authenticated user.
    
    return {
        }

    
@app.get("/FutureSQR/rest/user/csrf", response_class=JSONResponse)
def getCrsfToken():
    return {
            'headerName': "HeaderName",
            'parameterName': "ParameterName",
            'token': "CRSFTOKEN"
        }
    

@app.post("/FutureSQR/rest/user/logout", response_class=JSONResponse)
def postLogoutData(
        username: str = Form(...)
        ):
    
    sessionDB.userAuthenticationClear(username)
    
    return {
        }



### #########################################
###
### ADMINSTUFF
###
### #########################################


@app.get("/FutureSQR/rest/user/adminuserlist", response_class=JSONResponse)
def getUserManagementUserList():
    # this one is meant to be used for administration purposes.
    return usersDB.selectAllUSers()

@app.post("/FutureSQR/rest/user/ban", response_class=JSONResponse)
def banUser(userUuid:str = Form(...)):
    # TODO check user token, if eligible
    
    # TODO check if yourself, you should not be able to ban yourself
    
    if usersDB.hasUserByUUID(userUuid):
        user = usersDB.banUser(userUuid)
        if user is not None:
            return user
        
    # TODO: banned / updated user entry
    return {}


@app.post("/FutureSQR/rest/user/unban", response_class=JSONResponse)
def unbanUser(userUuid:str = Form(...)):
    # TODO check user token, if eligible
    
    # TODO check if yourself, you should not be able to unban yourself
    
    if usersDB.hasUserByUUID(userUuid):
        user = usersDB.unbanUser(userUuid)
        if user is not None:
            return user

    # TODO: unbanned / updated user entry
    return {}


@app.post("/FutureSQR/rest/user/add", response_class=JSONResponse)
def addNewUser(
        userName:str = Form(...), 
        password:str = Form(...), 
        displayName:str = Form(...),
        contactEmail:str = Form(...)):
    
    # TODO check user token, if eligible
    if usersDB.hasUserByLogonNme(userName):
        return False
    
    userrow = usersDB.insertNewUser(userName, displayName, contactEmail)
    
    # TODO: assign password to this user.
    # in case of a different password based backend  (e.g. LDAP), we may not need the password at all.
    
    return userrow
