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

class ReviewDatabase(object):
    '''
    classdocs
    '''

    def __init__(self, params:dict):
        '''
        Constructor
        '''
        self.reviewTable = {}
        self.revisionTable = {}

    def insertReview(self, project_id, review):
        if not project_id in self.reviewTable:
            self.reviewTable[project_id] = {}
        if not project_id in self.revisionTable:
            self.revisionTable[project_id] = {}
        # insert the revisions into a revision (reverse lookup) table 
        for revision_id in review[REVIEW_REVISIONS]:
            self.revisionTable[project_id][revision_id] = review[ REVIEW_PK_REVIEW_ID]
        # 
        self.reviewTable[project_id][ review[ REVIEW_PK_REVIEW_ID] ] = review
    
    def selectReviewByReviewId(self, project_id, review_id):
        if not project_id in self.reviewTable:
            return None
        if not review_id in self.reviewTable[project_id]:
            return None 
        return self.reviewTable[project_id][review_id]
    
    
    def selectOpenReviewsByProjectId(self, project_id):
        if not project_id in self.reviewTable:
            return []
        
        result = [ review for review in self.reviewTable[project_id].values() if review[REVIEW_LIFECYLCE_STATE] == REVIEW_LIFECYCLE_STATE_OPEN ]
        return result
    
    def selectClosedReviewsByProjectId(self, project_id):
        if not project_id in self.reviewTable:
            return []
        
        result = [ review for review in self.reviewTable[project_id].values() if review[REVIEW_LIFECYLCE_STATE] == REVIEW_LIFECYCLE_STATE_CLOSED ]
        return result
        
    
    def updateCloseReviewByReviewId(self, project_id, review_id):
        if not project_id in self.reviewTable:
            return None
        if not review_id in self.reviewTable[project_id]:
            return None 
        
        self.reviewTable[project_id][review_id][REVIEW_LIFECYLCE_STATE]=REVIEW_LIFECYCLE_STATE_CLOSED
        return None
    
    def updateReopenReviewByReviewId(self, project_id, review_id):
        if not project_id in self.reviewTable:
            return None
        if not review_id in self.reviewTable[project_id]:
            return None 
        
        self.reviewTable[project_id][review_id][REVIEW_LIFECYLCE_STATE]=REVIEW_LIFECYCLE_STATE_OPEN
        return None
    
    def updateDeleteReviewByReviewId(self, project_id, review_id):
        if not project_id in self.reviewTable:
            return None
        if not review_id in self.reviewTable[project_id]:
            return None 
        #if not project_id in self.revisionTable:
        #    self.revisionTable[project_id] = {}
        
        # delete revision list
        #         revision_list = self.reviewTable[project_id][review_id][REVIEW_REVISIONS]
        #         self.reviewTable[project_id][review_id][REVIEW_REVISIONS]=[]
        #         # set to deleted
        #         self.reviewTable[project_id][review_id][REVIEW_LIFECYLCE_STATE]=REVIEW_LIFECYCLE_STATE_DELETED
        #         
        #         # free up revision from revision list
        #         for revision_id in revision_list:
        #             self.revisionTable[project_id].pop(revision_id,None)
            
        return None

    def insertReviewerToReview(self, project_id, review_id, reviewer_id):
        if not project_id in self.reviewTable:
            return None
        if not review_id in self.reviewTable[project_id]:
            return None 
        
        currentReviewersMap = self.reviewTable[project_id][review_id][REVIEW_REVIEWERRESULTS]
        if not reviewer_id in currentReviewersMap:
            empty_review_result=self.__createReviewResult(reviewer_id,'');
            self.reviewTable[project_id][review_id][REVIEW_REVIEWERRESULTS][reviewer_id] = empty_review_result
            
        return None
    
    def __createReviewResult(self, reviewer_id, result):
        return {
                'reviewer_id':reviewer_id,
                'reviewresult':result
            }
        
    def approveReview(self, project_id, review_id, reviewer_id):
        if not project_id in self.reviewTable:
            return None
        if not review_id in self.reviewTable[project_id]:
            return None 
        currentReviewersMap = self.reviewTable[project_id][review_id][REVIEW_REVIEWERRESULTS]
        if not reviewer_id in currentReviewersMap:
            return None
        
        approve_review_result=self.__createReviewResult(reviewer_id,'approve');
        self.reviewTable[project_id][review_id][REVIEW_REVIEWERRESULTS][reviewer_id] = approve_review_result
        return None
    
    def concernReview(self, project_id, review_id, reviewer_id):
        if not project_id in self.reviewTable:
            return None
        if not review_id in self.reviewTable[project_id]:
            return None 
        currentReviewersMap = self.reviewTable[project_id][review_id][REVIEW_REVIEWERRESULTS]
        if not reviewer_id in currentReviewersMap:
            return None
        
        concern_review_result=self.__createReviewResult(reviewer_id,'concern');
        self.reviewTable[project_id][review_id][REVIEW_REVIEWERRESULTS][reviewer_id] = concern_review_result
        return None
    
    def addRevisionToReview(self, project_id, review_id, revision_id):
        if not project_id in self.reviewTable:
            return None
        if not project_id in self.revisionTable:
            return None
        if not review_id in self.reviewTable[project_id]:
            return None
        
        self.reviewTable[project_id][review_id][REVIEW_REVISIONS].append(revision_id) 
        self.revisionTable[project_id][revision_id] = review_id

        return None

    def hasReviewByRevisionId(self, project_id, revision_id):
        if not project_id in self.revisionTable:
            return False
        if not revision_id in self.revisionTable[project_id]:
            return False
        return True

    def selectReviewIdByRevisionId(self, project_id, revision_id):
        if not project_id in self.revisionTable:
            return None
        if not revision_id in self.revisionTable[project_id]:
            return None
        return self.revisionTable[project_id][revision_id]
    
    def getRevisionToReviewsMap(self, project_id):
        if not project_id in self.revisionTable:
            return {}
        return self.revisionTable[project_id]
    
    def selectRevisionsForReview(self, project_id, review_id):
        if not project_id in self.reviewTable:
            return None
        if not review_id in self.reviewTable[project_id]:
            return None
        return self.reviewTable[project_id][review_id][REVIEW_REVISIONS]
    