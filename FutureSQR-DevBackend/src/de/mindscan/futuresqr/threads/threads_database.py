'''
Created on 24.07.2022

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

import uuid
from de.mindscan.futuresqr.threads.thread_table_columns import * # @UnusedWildImports

class ThreadsDatabase(object):
    '''
    classdocs
    '''


    def __init__(self, params):
        '''
        Constructor
        '''
        self.threadTable = {}
        self.messageTable = {}
    
    def createNewThread(self, message, author_uuid):
        thread_uuid = self.__create_uuid()
        
        # create a new root message
        message_uuid = self.createRootMessage(thread_uuid, message, author_uuid)
        
        thread = {
                THREADS_PK_THREAD_ID: thread_uuid,
                THREADS_FK_AUTHOR_ID: author_uuid,
                # register message_uuid in threadid        
                # so messages can be resolved forward and backward
                THREADS_FK_MESSAGES    : [message_uuid]
             }
        
        # provide some meta information for the thread
        # like when was it created,
        # by whom etc.
        
        # register current thread in in-memory thread table
        self.threadTable[thread_uuid] = thread
        return thread_uuid
    
    def createRootMessage(self, thread_uuid, message_text, author_uuid):
        message_uuid = self.__create_uuid()
        
        msg_row = {
                MESSAGES_PK_MESSAGES_ID: message_uuid,
                MESSAGES_FK_THREAD_ID  : thread_uuid,
                MESSAGES_MESSAGE       : message_text,
                MESSAGES_FK_AUTHOR_ID  : author_uuid,
                MESSAGES_REPLY_TO_ID   : ''
            }
        
        # set some initial message state
        # set reply to empty or to threadid?
        
        self.messageTable[message_uuid] = msg_row
        return message_uuid
    
    def createMessageResponse(self, thread_uuid, replyto_messageid, message_text, author_uuid):
        # create a new message uuid
        message_uuid = self.__create_uuid()

        msg_row = {
                MESSAGES_PK_MESSAGES_ID: message_uuid,
                MESSAGES_FK_THREAD_ID  : thread_uuid,
                MESSAGES_MESSAGE       : message_text,
                MESSAGES_FK_AUTHOR_ID  : author_uuid,
                MESSAGES_REPLY_TO_ID   : replyto_messageid
            }
        
        # set some initial message state

        # add this message to the list of messages related to the thread.        
        self.threadTable[thread_uuid][THREADS_FK_MESSAGES].append(message_uuid)
        
        self.messageTable[message_uuid] = msg_row         
        pass
    
    
    def updateMessage(self, message_uuid, newmessage):
        # set message text to updated message
        # set some edited flag
        # set last updated date
        pass
    
    
    def selectFullThread(self, threadid):
        if not threadid in  self.threadTable:
            return {}
        
        result = self.threadTable[threadid].copy()
        result[THREADS_MESSAGES] = { key:self.messageTable[key].copy() for key in self.threadTable[threadid][THREADS_FK_MESSAGES] }
        
        return result
    
    def __create_uuid(self):
        return uuid.uuid4().hex