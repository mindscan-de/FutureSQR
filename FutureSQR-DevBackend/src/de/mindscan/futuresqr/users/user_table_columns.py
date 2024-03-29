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


USER_UUID           = 'uuid'
USER_PK_USERID      = USER_UUID
USER_LOGON_NAME     = 'loginname'
USER_DISPLAYNAME    = 'displayname'
USER_AVATARLOCATION = 'avatarlocation'
USER_CONTACT_EMAIL  = 'email'
USER_ISBANNED       = 'isbanned'
USER_CREATEDDATE    = 'created'
USER_MODIFIEDDATE   = 'modified'
USER_BANNEDDATE     = 'banned'

# Actually we have the user token based authentication
# but we also provide a session information, which must 
# be able to continue (resume) the last session or re-
# establish a new userauth session
USERAUTHN_FK_USERID    = 'useruuid'
USERAUTHN_AUTHNMETHOD  = 'authmethod'
USERAUTHN_AUTHNPARAMS  = 'authparams'
USERAUTHN_MODIFIEDDATE = 'modified'

# TOOD: 
USERAUTHNSESSION_FK_USERID = ''
USERAUTHNSESSION_STARTED = ''
USERAUTHNSESSION_EXPIRES = ''
# USERAUTH_REAUTHMETHOD = 'reauthmethod'
# USERAUTH_REAUTHPARAMS