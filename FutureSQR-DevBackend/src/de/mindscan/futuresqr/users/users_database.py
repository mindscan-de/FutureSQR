'''
Created on 20.08.2022

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
from de.mindscan.futuresqr.users.user_table_columns import *  # @UnusedWildImport

class UsersDatabase(object):
    '''
    classdocs
    '''


    def __init__(self, params):
        '''
        Constructor
        '''
        self._userMap = {}
    
    def getUserByUUID(self, uuid: str):
        if(uuid in self._userMap):
            return self._userMap[uuid]
        return None
    
    def getUserByLogonName(self, logonname:str):
        for user in self._userMap:
            if user[USER_LOGON_NAME] == logonname:
                return user
        return None
    
    def insertNewUser(self, logonname, displayname):
        pk_uuid = str(uuid.uuid4())
        
        userRow = {
                USER_UUID: pk_uuid,
                USER_LOGON_NAME: logonname,
                USER_DISPLAYNAME: displayname,
                USER_AVATARLOCATION: pk_uuid + ".png",
                USER_ISBANNED: False,
                # USER_CREATEDDATE: now()
                # USER_MODIFIEDDATE: 0
                # USER_BANNEDDATE: 0
            }
        
        self._userMap[pk_uuid] = userRow
        
        return userRow