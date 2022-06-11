'''
Created on 05.06.2022

@author: JohnDoe
'''


from de.mindscan.futuresqr.gittools.dev_local_git_access import calculateRecentRevisionsForLocalGitRepo, calculateDiffForSingleRevision
from de.mindscan.futuresqr.assets.hardcoded import getAllProjectToLocalPathMap, getAllStarredProjectsForUser, getAllProjectsForUser

from fastapi import FastAPI, Form, HTTPException

app = FastAPI()


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
        return calculateRecentRevisionsForLocalGitRepo(project_path_translation[projectid])
    
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