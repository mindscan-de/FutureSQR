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

from de.mindscan.futuresqr.reviews.review_tables_columns import *  # @UnusedWildImport
from de.mindscan.futuresqr.users.users_lookup import name_to_uuid

def createNewReview(projectDB, projectId, revisionInformation) :
    reviewIdentifier = projectDB.calculateNewReviewIndex(projectId)
    
    if reviewIdentifier is None:
        return None
    
    review = {
        REVIEW_PK_REVIEW_ID : reviewIdentifier,
        REVIEW_TITLE : revisionInformation['firstCommitLine'],
        REVIEW_ADDITIONAL_DESCRIPTION : "",
        # WHENEVER WE CHANGE THE revision configuration -- WE MUST UPDATE THE AUTHORS LIST
        REVIEW_REVISIONS : [revisionInformation['revisionID']],
        REVIEW_AUTHORS : [name_to_uuid(revisionInformation['author'])],
        REVIEW_READY_TO_CLOSE : False,
        REVIEW_UNASSIGNED : True,
        # This is more complicated i guess.. (right now)
        REVIEW_REVIEWERRESULTS : {},
        # 
        REVIEW_FK_PROJECT_ID : projectId,
        REVIEW_LIFECYLCE_STATE : REVIEW_LIFECYCLE_STATE_OPEN
        }
    
    return review
