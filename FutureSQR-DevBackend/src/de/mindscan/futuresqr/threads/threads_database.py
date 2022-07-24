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
    
    def createNewThread(self, message):
        # TODO: create a thread_uuid
        # provide some meta information for the thread
        # like when was it created,
        # by whom etc.
        
        # TODO: then create a new root message
        # TODO: then add the message to the thread as well
        # TODO: so messages can be resolved forward and backward
        pass
    
    def createRootMessage(self, threadid, message):
        # create a new message uuid
        # author
        # message
        # set some initial message state
        # set the threadid
        # set reply to empty or to threadid?
        # register message to threadid
        pass
    
    def createMessageResponse(self, threadid, replyto_messageid, message):
        # create a new message uuid
        # author
        # message
        # set some initial message state
        # set the threadid
        # set the reply to id to
        
        # register message to threadid
        pass
    
    
    def updateMessage(self, messageid, newmessage):
        # set some edited flag
        # set last updated date
        pass
    
    
    def selectFullThread(self, threadid):
        # TODO: find thread
        # TODO: find all messages
        pass