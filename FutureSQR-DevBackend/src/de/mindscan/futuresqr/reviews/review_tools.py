'''
Created on 23.06.2022

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

from de.mindscan.futuresqr.assets.hardcoded import getProjectConfigurations
from de.mindscan.futuresqr.reviews.review_tables_columns import *  # @UnusedWildImport

projectConfigurations = { }

def initProjectConfigurations():
    global projectConfigurations
    projectConfigurations = getProjectConfigurations();
    pass

def createNewReview(projectConfiguration, revisionInformation) :
    newReviewId = projectConfiguration['autoIndex']
    projectConfiguration['autoIndex']+=1
    reviewIdentifier = projectConfiguration['reviewPrefix'] +  str(newReviewId)
    
    review = {
        REVIEW_PK_REVIEW_ID : reviewIdentifier,
        REVIEW_TITLE : revisionInformation['firstCommitLine'],
        REVIEW_ADDITIONAL_DESCRIPTION : "",
        # WHENEVER WE CHANGE THE revision configuration -- WE MUST UPDATE THE AUTHORS LIST
        REVIEW_REVISIONS : [revisionInformation['revisionID']],
        # Actually this should be a uuid for each author
        REVIEW_AUTHORS : ['mindscan-de'],
        # 
        REVIEW_FK_PROJECT_ID : projectConfiguration['projectID'],
        REVIEW_LIFECYLCE_STATE : REVIEW_LIFECYCLE_STATE_OPEN
        }
    
    return review
