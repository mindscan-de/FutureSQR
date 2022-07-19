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

def getProjectConfigurations(): 
        return {
            'furiousiron-frontend' : {
                'autoIndex':1,
                'reviewPrefix':"CR-FI-FRNT-",
                'projectID':'furiousiron-frontend',
                'projectDisplayName':'FuriousIron-Frontend',
                'projectDescription': 'My personal source code engine project. Frontend. (Angular. TS)',
                'projectIsStarred':False,
                'projectBranchName':'master', # polluted by git scm, should be part of a SCM configuration
                'administration': {
                    'localPath':"D:\\Temp\\future-square-cache\\FuriousIron-Frontend",
                    }
                },
                
            'furiousiron-hfb' : {
                'autoIndex':50,
                'reviewPrefix':"CR-FI-HFB-",
                'projectID':'furiousiron-hfb',
                'projectDisplayName':'FuriousIron-HFB',
                'projectDescription':'Hash-Free Bloom-Filter (Proof of concept implementation)',
                'projectIsStarred':False,
                'projectBranchName':'main', # polluted by git scm, should be part of a SCM configuration
                'administration':{
                    'localPath':"D:\\Temp\\future-square-cache\\FuriousIron-HFB",
                    }
                },
                
            'furiousiron-indexer' : {
                'autoIndex':1,
                'reviewPrefix':'CR-FI-NDX-',
                'projectID':'furiousiron-indexer',
                'projectDisplayName':'FuriousIron-Indexer',
                'projectDescription':'My personal source code search engine project. Indexer. (Java. Windows. No Database. Filesystem only)',
                'projectIsStarred':False,
                'administration':{
                    'localPath':None
                    }
                },
                
            'furiousiron-searchbackend' : {
                'autoIndex':1,
                'reviewPrefix':'CR-FI-SRND-',
                'projectID':'furiousiron-searchbackend',
                'projectDisplayName':'FuriousIron-SearchBackend',
                'projectDescription':'My personal source code search engine project. Backend. (Java. Tomcat. Windows. No Database. Filesystem only)',
                'projectIsStarred':False,
                'administration':{
                    'localPath':None
                    }
                },
                
            'futuresqr': {
                'autoIndex':100,
                'reviewPrefix':"CR-FSQR-",
                'projectID':'futuresqr',
                'projectDisplayName':'FutureSQR',
                'projectDescription':'Future Source Quality Review -- Code Review Tool for Trunk-Based-Development',
                'projectIsStarred':False,
                'projectBranchName':'main', # polluted by git scm, should be part of a SCM configuration
                'administration': {
                    'localPath':"D:\\Temp\\future-square-cache\\FutureSQR",                        
                    }
                },
                
            'brightflux' : {
                'autoIndex':1,
                'reviewPrefix':'CR-BRFX-',
                'projectID':'brightflux',
                'projectDisplayName':'BrightFlux',
                'projectDescription':'LogFileViewer and LogFileAnalysis with yet unseen Features and written in Java',
                'projectIsStarred':False,
                'administration':{
                    'localPath':None
                    }
                },

            'curiousmyth' : {
                'autoIndex':1,
                'reviewPrefix':'CR-CRSM-',
                'projectID':'curiousmyth',
                'projectDisplayName':'CuriousMyth',
                'projectDescription':'Modelling facts, entities and information for knowledge representation',
                'projectIsStarred':False,
                'administration':{
                    'localPath':None
                    }
                },
                
            'orangemoon-frontend' : {
                'autoIndex':1,
                'reviewPrefix':'CR-ORM-FND-',
                'projectID':'orangemoon-frontend',
                'projectDisplayName':'OrangeMoon-Frontend',
                'projectDescription':'Japanese Dictionary Web-App - Frontend (based on nodejs and angular)',
                'projectIsStarred':False,
                'administration':{
                    'localPath':None
                    }
                },

            'orangemoon-backend' : {
                'autoIndex':1,
                'reviewPrefix':'CR-ORM-BND-',
                'projectID':'orangemoon-backend',
                'projectDisplayName':'OrangeMoon-Backend',
                'projectDescription':'Japanese Dictionary Web-App - Backend (based on fastapi and jamdict)',
                'projectIsStarred':False,
                'administration':{
                    'localPath':None
                    }
                },

            'fluentgenesis-embedder' : {
                'autoIndex':1,
                'reviewPrefix':'CR-FLUGEN-EMB-',
                'projectID':'fluentgenesis-embedder',
                'projectDisplayName':'FluentGenesis-Embedder',
                'projectDescription':'Source Code Language Unserstanding - Calculating the Embedding vectors and such.',
                'projectIsStarred':False,
                'administration':{
                    'localPath':None
                    }
                },

            'fluentgenesis-classifier' : {
                'autoIndex':1,
                'reviewPrefix':'CR-FLUGEN-CLS-',
                'projectID':'fluentgenesis-classifier',
                'projectDisplayName':'FluentGenesis-Classifier',
                'projectDescription':'',
                'projectIsStarred':False,
                'administration':{
                    'localPath':None
                    }
                },

            'fluentgenesis-plugin' : {
                'autoIndex':1,
                'reviewPrefix':'CR-FLUGEN-PLG-',
                'projectID':'fluentgenesis-plugin',
                'projectDisplayName':'FluentGenesis-Plugin',
                'projectDescription':'Source Code Language Understanding - Eclipse plugin for Source Code Generation.',
                'projectIsStarred':False,
                'administration':{
                    'localPath':None
                    }
                },

                
            }