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

    def insertReview(self, project_id, review):
        if not project_id in self.reviewTable:
            self.reviewTable[project_id] = {}
        self.reviewTable[project_id][ review[ REVIEW_PK_REVIEW_ID] ] = review
        
    def selectReviewByReviewId(self, project_id, review_id):
        if not project_id in self.reviewTable:
            return None
        if not review_id in self.reviewTable[project_id]:
            return None 
        return self.reviewTable[project_id][review_id]

