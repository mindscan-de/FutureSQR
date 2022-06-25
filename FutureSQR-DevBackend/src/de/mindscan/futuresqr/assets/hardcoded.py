'''
Created on 09.06.2022

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

import json

TEMP_ASSET_FOLDER = '../../../../../tempassets/'

'''
Purpose of this module:

This module will provide either hard coded assets or hard coded data, which
should be retrieved through a database or some other kind of complex calculation.
Sometimes we just don't need to solve this particular problem until we know
we want to go that route. Also this is the development backend, which should
provide just enough reasonable data to develop the ui / frontend. We may
need to solve this problem later - but that means we can still go fast, and 
do not need to solve everything by now.
'''


def _getFromTempAssets( filename:str ):
    '''
    Provide an asset stored as a json file in the temp asset folder.
    
    :param filename:
    '''
    with open(TEMP_ASSET_FOLDER+str(filename),'r') as inputfile:
        return json.load(inputfile)
    print("something went wrong with "+str(filename) )
    return {}

def getAllProjectToLocalPathMap():
    return {
    'furiousiron-frontend':"D:\\Temp\\future-square-cache\\FuriousIron-Frontend",
    'futuresqr':"D:\\Temp\\future-square-cache\\FutureSQR",
    'furiousiron-hfb':"D:\\Temp\\future-square-cache\\FuriousIron-HFB"
    }


def getAllStarredProjectsForUser():
    return [
        {"project_id":"furiousiron-frontend", "project_name":"FuriousIron-Frontend", "description":"My personal source code engine project. Frontend. (Angular. TS)"},
        {"project_id":"furiousiron-hfb", "project_name":"FuriousIron-HFB", "description":"Hash-Free Bloom-Filter (Proof of concept implementation)"},
        {"project_id":"furiousiron-indexer", "project_name":"FuriousIron-Indexer", "description":"My personal source code search engine project. Indexer. (Java. Windows. No Database. Filesystem only) "},
        {"project_id":"furiousiron-searchbackend", "project_name":"FuriousIron-SearchBackend", "description":"My personal source code search engine project. Backend. (Java. Tomcat. Windows. No Database. Filesystem only) "},
        {"project_id":"futuresqr", "project_name":"FutureSQR", "description":"Code Review Tool - Future Source Quality Review"},
        {"project_id":"orangemoon-frontend", "project_name":"OrangeMoon-Frontend", "description":"Japanese Dictionary Web-App - Frontend (based on nodejs and angular)"},
        {"project_id":"orangemoon-backend", "project_name":"OrangeMoon-Backend", "description":"Japanese Dictionary Web-App - Backend (based on fastapi and jamdict) "}
        ]

def getAllProjectsForUser():
    return [
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
    
def getProjectConfigurations(): 
        return {
            'furiousiron-frontend' : {
                    'autoIndex':1,
                    'reviewPrefix':"CR-FI-FRNT-",
                    'projectID':'furiousiron-frontend'
                },
            'futuresqr': {
                    'autoIndex':100,
                    'reviewPrefix':"CR-FSQR-",
                    'projectID':'futuresqr'
                },
            'furiousiron-hfb' : {
                    'autoIndex':50,
                    'reviewPrefix':"CR-FI-HFB-",
                    'projectID':'furiousiron-hfb'
                }
            }