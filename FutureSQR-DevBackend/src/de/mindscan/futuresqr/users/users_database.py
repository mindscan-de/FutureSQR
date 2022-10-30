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
from de.mindscan.futuresqr.assets.hardcoded import _getFromTempAssets, _putToTempAssets, USERNAMES_NAMESPACE_OID

class UsersDatabase(object):
    '''
    classdocs
    '''


    def __init__(self, params:dict):
        '''
        Constructor
        '''
        self.__persistence = False
        if 'persistenceActive' in params:
            self.__persistence = params['persistenceActive']
        
        self._userMap = {}
        
        userdatabase = _getFromTempAssets('userdatabase.json')
        
        if(len(userdatabase) == 0):
            self.insertNewUser('mindscan-de', 'Maxim Gansert', 'contact@themail.local')
            self.insertNewUser('mindscan-banned', 'Maxim Gansert Banned Testuser', 'mindscan@local.localhost')
            self.insertNewUser('someoneelsa', 'Elsa Someone', 'contact@elsamail.local')
        else:
            self._userMap = userdatabase
        
        # TODO: if tempassets/user_database.json exist -> load this...
        # TODO: also use a flag whether we want to persist things or not.
        
    
    def getUserByUUID(self, uuid: str):
        if self.hasUserByUUID(uuid):
            return self._userMap[uuid]
        return None
    
    def getUserByLogonName(self, logonname:str):
        for user in self._userMap.values():
            if user[USER_LOGON_NAME] == logonname:
                return user
        return None
    
    def hasUserByUUID(self,uuid: str):
        return uuid in self._userMap
    
    def hasUserByLogonNme(self, logonname:str):
        return str(self.__uuidFromName(logonname)) in self._userMap
    
    def insertNewUser(self, logonname, displayname, contactemail):
        pk_uuid = str(self.__uuidFromName(logonname))
        
        # TOOD: check if id already known in userdatabase.
        
        userRow = {
                USER_UUID: pk_uuid,
                USER_LOGON_NAME: logonname,
                USER_DISPLAYNAME: displayname,
                USER_AVATARLOCATION: pk_uuid + ".png",
                USER_CONTACT_EMAIL: contactemail,
                USER_ISBANNED: False,
                # USER_CREATEDDATE: now()
                # USER_MODIFIEDDATE: 0
                # USER_BANNEDDATE: 0
            }
        
        self._userMap[pk_uuid] = userRow
        self.__persist_userdatabase()
        return userRow
    
    def selectAllUSers(self):
        # TODO sort by some criteria
        return list(self._userMap.values())
    
    def getAsSimpleUserMap(self, userlist):
        return {
            user[USER_UUID] : {
                USER_UUID:user[USER_UUID], 
                USER_DISPLAYNAME:user[USER_DISPLAYNAME],
                USER_AVATARLOCATION:user[USER_AVATARLOCATION],
                USER_ISBANNED:user[USER_ISBANNED]
             } for user in userlist }
    
    def banUser(self, userUuid:str):
        '''
        Ban a user by UUID.
        
        :param userUuid: UUID of the User to be banned.
        
        @return: returns either None (if user not present), or the updated user entry
        '''
        if userUuid in self._userMap:
            self._userMap[userUuid][USER_ISBANNED] = True
            self.__persist_userdatabase()                
            return self._userMap[userUuid]
        return None
    
    def unbanUser(self, userUuid: str):
        '''
        Remove a ban of a user by UUID.
        
        :param userUuid: UUID of the User to be unbanned.
        
        @return: returns either None (if user not present), or the updated user entry
        '''
        
        if userUuid in self._userMap:
            self._userMap[userUuid][USER_ISBANNED] = False
            self.__persist_userdatabase()
            return self._userMap[userUuid]
        return None
    
    def updateContactEmail(self, logonname:str, contactemail:str):
        uuid = str(self.__uuidFromName(logonname))
        if uuid in self._userMap:
            self._userMap[uuid][USER_CONTACT_EMAIL] = contactemail
            self.__persist_userdatabase()
            return self._userMap[uuid]
        return None
    
    def updateDisplayName(self, logonname:str, displayname:str):
        uuid = str(self.__uuidFromName(logonname))
        if uuid in self._userMap:
            self._userMap[uuid][USER_DISPLAYNAME] = displayname
            self.__persist_userdatabase()
            return self._userMap[uuid]
        
        return None
        
    def __persist_userdatabase(self): 
        if self.__persistence:
            _putToTempAssets(self._userMap, 'userdatabase.json')
        pass
    
    def __uuidFromName(self, username):
        return uuid.uuid3(USERNAMES_NAMESPACE_OID, username)
