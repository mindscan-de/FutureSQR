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

from fastapi import FastAPI, Form, HTTPException
from pip._internal.pyproject import make_pyproject_path

from de.mindscan.futuresqr.gittools.dev_local_git_access import calculateRecentRevisionsForLocalGitRepo, calculateDiffForSingleRevision, calculateFileListForSigleRevision
from de.mindscan.futuresqr.assets.hardcoded import getAllProjectToLocalPathMap, getAllStarredProjectsForUser, getAllProjectsForUser, getRevisionToReviewMap, getProjectConfigurations
from de.mindscan.futuresqr.reviews.review_database import ReviewDatabase
from de.mindscan.futuresqr.projects.project_database import ProjectDatabase
from de.mindscan.futuresqr.reviews.review_tools import createNewReview
from de.mindscan.futuresqr.reviews.review_tables_columns import *  # @UnusedWildImport


app = FastAPI()

reviewDB = ReviewDatabase({})
projectDB = ProjectDatabase({'allProjects':getProjectConfigurations()})



@app.get("/")
def read_root():
    return {"message":"Hello World! It works! But now, go away!"}

@app.get("/FutureSQR/rest/user/starredprojects")
def getUserStarredProjects(user_uuid:str = ""):
    result = getAllStarredProjectsForUser() 
    return result

@app.get("/FutureSQR/rest/user/allaccessibleprojetcs")
def getUserAllAccessibleProjects(user_uuid: str = ""):
    result = getAllProjectsForUser()
    return result

@app.get("/FutureSQR/rest/project/{projectid}/recentcommits")
def getProjectRevisions(projectid:str):
    project_path_translation = getAllProjectToLocalPathMap()
    if projectid in project_path_translation:
        # TODO: cache this answer for some time and/or limit the number of results?
        revisions = calculateRecentRevisionsForLocalGitRepo(project_path_translation[projectid])
        # combine revisions with a review list for the revisons and add the revision id to the revision list
        revision_map = getRevisionToReviewMap()
        
        for revision in revisions['revisions']:
            if revision['revisionid'] in revision_map:
                revision['hasReview']= True
                revision['reviewID']= revision_map[revision['revisionid']]
            else:
                revision['hasReview']= False
        
        return revisions
    
    # TODO: we should say that this repository is not yet available?        
    result = {'revisions':[]}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/recentreviews")
def getProjectReviews(projectid:str):
    project_path_translation = getAllProjectToLocalPathMap()
    if projectid in project_path_translation:
        pass
    
    result = {
#         'projectinfo' : {
#                 'projectName':'The Full Project Name'
#             }
        'reviews':[]
        }
    return result

@app.get("/FutureSQR/rest/project/{projectid}/revisiondiff/{revisionid}")
def getProjectRevisionDiffToPrevious(projectid:str, revisionid:str):
    project_path_translation = getAllProjectToLocalPathMap()
    
    if projectid in project_path_translation:
        result = calculateDiffForSingleRevision(project_path_translation[projectid], revisionid)
        return result
    
    result = {}
    return result


@app.get("/FutureSQR/rest/project/{projectid}/filelist/{revisionid}")
def getProjectRevisionListeListDiffToPrevious(projectid:str, revisionid:str):
    project_path_translation = getAllProjectToLocalPathMap()
    
    if projectid in project_path_translation:
        result = calculateFileListForSigleRevision(project_path_translation[projectid], revisionid)
        return result
    
    result = {}
    return result

### #########################################
###
### Some Review functions - non persistent
###
### #########################################

# to create a new review from a revision
@app.post("/FutureSQR/rest/project/{projectid}/review/create")
def postCreateNewReview(projectid:str, revisionid:str = Form(...)):
    project_path_translation = getAllProjectToLocalPathMap()
    
    if projectid in project_path_translation:
        # * get someinformation about this particular version heading and so on for the review title
        revisionInformation = {
                'firstCommitLine': "let this be the first line",
                'revisionID':revisionid
            }
        
        # TODO: check if the review revisionid is already covered by a review
        
        # project confioguration should have an autoincrementing index, which is the truth for the creation of reviews.
        projectConfiguration = projectDB.getProjectConfiguration(projectid);
        
        # * we then create a new review in the backend
        #   * we get then a new unique review ID back
        newReview = createNewReview(projectConfiguration, revisionInformation)
        
        reviewDB.insertReview(projectid, newReview)
        
        result = {
                'projectid':projectid,
                'revisionid':revisionid,
                'reviewid':newReview[REVIEW_PK_REVIEW_ID],
                'reviewdata':newReview
            }
        return result
    
    result = {}
    return result
    
    
    
@app.post("/FutureSQR/rest/project/{projectid}/review/close")
def postCloseReview(projectid:str, reviewid:str = Form(...)):
    project_path_translation = getAllProjectToLocalPathMap()
    
    if projectid in project_path_translation:
        # TODO. review should exist in project
        #
        pass
    
    result = {}
    return result
    
@app.post("/FutureSQR/rest/project/{projectid}/review/delete")
def postDeleteReview(projectid:str, reviewid:str = Form(...)):
    project_path_translation = getAllProjectToLocalPathMap()
    
    if projectid in project_path_translation:
        # TODO. review should exist in project
        #
        pass
    
    result = {}
    return result
    
    
@app.post("/FutureSQR/rest/project/{projectid}/review/reopen")
def postReopenReview(projectid:str, reviewid:str=Form(...)):
    project_path_translation = getAllProjectToLocalPathMap()
    
    if projectid in project_path_translation:
        # TODO. review should exist in project
        #
        pass
    
    result = {}
    return result

