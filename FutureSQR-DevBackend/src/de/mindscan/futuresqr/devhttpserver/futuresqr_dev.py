'''
Created on 05.06.2022

@author: JohnDoe
'''

import json

from de.mindscan.futuresqr.gittools.dev_local_git_access import calculateRecentRevisionsForLocalGitRepo 

from fastapi import FastAPI, Form, HTTPException

TEMP_ASSET_FOLDER = '../../../../../tempassets/'

app = FastAPI()

project_path_translation = {
    'furiousiron-frontend':"D:\\Temp\\future-square-cache\\FuriousIron-Frontend",
    'futuresqr':"D:\\Temp\\future-square-cache\\FutureSQR",
    'furiousiron-hfb':"D:\\Temp\\future-square-cache\\FuriousIron-HFB"
    }

@app.get("/")
def read_root():
    return {"message":"Hello World! It works! But now, go away!"}

@app.get("/FutureSQR/rest/user/starredprojects")
def getUserStarredProjects(user_uuid:str = ""):
    result = [
        {"project_id":"furiousiron-frontend", "project_name":"FuriousIron-Frontend", "description":"My personal source code engine project. Frontend. (Angular. TS)"},
        {"project_id":"furiousiron-hfb", "project_name":"FuriousIron-HFB", "description":"Hash-Free Bloom-Filter (Proof of concept implementation)"},
        {"project_id":"furiousiron-indexer", "project_name":"FuriousIron-Indexer", "description":"My personal source code search engine project. Indexer. (Java. Windows. No Database. Filesystem only) "},
        {"project_id":"furiousiron-searchbackend", "project_name":"FuriousIron-SearchBackend", "description":"My personal source code search engine project. Backend. (Java. Tomcat. Windows. No Database. Filesystem only) "},
        {"project_id":"futuresqr", "project_name":"FutureSQR", "description":"Code Review Tool - Future Source Quality Review"},
        {"project_id":"orangemoon-frontend", "project_name":"OrangeMoon-Frontend", "description":"Japanese Dictionary Web-App - Frontend (based on nodejs and angular)"},
        {"project_id":"orangemoon-backend", "project_name":"OrangeMoon-Backend", "description":"Japanese Dictionary Web-App - Backend (based on fastapi and jamdict) "}
    ]
    return result

@app.get("/FutureSQR/rest/user/allaccessibleprojetcs")
def getUserAllAccessibleProjects(user_uuid: str = ""):
    result = [
        {"project_id":"furiousiron-frontend", "project_name":"FuriousIron-Frontend", "description":"My personal source code engine project. Frontend. (Angular. TS)"},
        {"project_id":"furiousiron-hfb", "project_name":"FuriousIron-HFB", "description":"Hash-Free Bloom-Filter (Proof of concept implementation)"},
        {"project_id":"furiousiron-indexer", "project_name":"FuriousIron-Indexer", "description":"My personal source code search engine project. Indexer. (Java. Windows. No Database. Filesystem only) "},
        {"project_id":"furiousiron-searchbackend", "project_name":"FuriousIron-SearchBackend", "description":"My personal source code search engine project. Backend. (Java. Tomcat. Windows. No Database. Filesystem only) "},
        {"project_id":"brightflux", "project_name":"BrightFlux", "description":"LogFileViewer and LogFileAnalysis with yet unseen Features and written in Java"},
        {"project_id":"futuresqr", "project_name":"FutureSQR", "description":"Code Review Tool - Future Source Quality Review"},
        {"project_id":"curiousmyth", "project_name":"CuriousMyth", "description":"Modelling facts, entities and information for knowledge representation"},
        {"project_id":"orangemoon-frontend", "project_name":"OrangeMoon-Frontend", "description":"Japanese Dictionary Web-App - Frontend (based on nodejs and angular)"},
        {"project_id":"orangemoon-backend", "project_name":"OrangeMoon-Backend", "description":"Japanese Dictionary Web-App - Backend (based on fastapi and jamdict) "},
        {"project_id":"fluentgenesis-embedder", "project_name":"FluentGenesis-Embedder", "description":""},
        {"project_id":"fluentgenesis-classifier", "project_name":"FluentGenesis-Classifier", "description":""},
        {"project_id":"fluentgenesis-plugin", "project_name":"FluentGenesis-Plugin", "description":""}
        ]
    return result

@app.get("/FutureSQR/rest/project/{projectid}/recentcommits")
def getProjectRevisions(projectid:str):
    if projectid in project_path_translation:
        # TODO: cache this answer for some time.
        return calculateRecentRevisionsForLocalGitRepo(project_path_translation[projectid])
            
    result = {'revisions':[]}
    return result

@app.get("/FutureSQR/rest/project/{projectid}/revisiondiff/{revisionid}")
def getProjectRevisionDiffToPrevious(projectid:str, revisionid:str):
    if projectid in project_path_translation:
        result = {}
        return result
    
    result = {}
    return result