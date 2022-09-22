'''
Created on 14.08.2022

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

from fastapi import FastAPI, Form, HTTPException, status
from de.mindscan.futuresqr.assets.passwd import getPasswdEntry
from de.mindscan.futuresqr.users.users_database import UsersDatabase

app = FastAPI()

userDatabase = UsersDatabase({});

@app.post("/FutureSQR/rest/user/authenticate")
def postLoginData(
        username:str = Form(...), 
        password:str = Form(...)):

    # find user by uername in user passwd database
    pwEntry = getPasswdEntry(username)
    if pwEntry is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="No such user or not authenticated"
            )
    # find userentry by username in user database
    userEntry = userDatabase.getUserByLogonName(username);

    # check if user is banned
    
    # verify password,
    # either we generate a valid user dataset response
    # or we handle the wrong password by how?
    
    
    return {
        'id': username
        }

@app.post("/FutureSQR/rest/user/add")
def addNewUser(
        username:str = Form(...), 
        password:str = Form(...), 
        displayname:str = Form(...),
        contactemail:str = Form(...)):
    
    # TODO check user token, if eligible
    if userDatabase.hasUserByLogonNme(username):
        return False
    
    userrow = userDatabase.insertNewUser(username, displayname, contactemail)
    
    # TODO: assign password to this user.
    # in case of a different password based backend  (e.g. LDAP), we may not need the password at all.
    pass

@app.post("/FutureSQR/rest/user/ban")
def banUser(
        username:str = Form(...)):
    # TODO check user token, if eligible
    if userDatabase.hasUserByLogonNme(username):
        userDatabase.banUser(username)
    pass

@app.post("/FutureSQR/rest/user/unban")
def unbanUser(
        username:str = Form(...)):

    # TODO check user token, if eligible
    if userDatabase.hasUserByLogonNme(username):
        userDatabase.unbanUser(username)
    pass

@app.post("FutureSQR/rest/user/updatecontact")
def updateContactEmail(
        username:str = Form(...),
        contactemail:str = Form(...)):
	# if admin, change the email for the username
	
    # TOOD: check user token if eligible username == logged in user
	
	# let the person receive the mail and then use the sent link to update 
	# the new contact address.
	
	# instead of changing it immediately, there should be an activation procedure, which proves that the logged in user is the owner of this email address
    if userDatabase.hasUserByLogonNme(username):
        userDatabase.updateContactEmail(username, contactemail)
    pass

@app.get("FutureSQR/rest/user/simplelist")
def getSimpleUserList():
    # the role of the user has to be checked for admin rights to retrieve
    # that list, even then the list should be filtered.
    
    return []

def updateDisplayName(
        username:str = Form(...),
        displayname:str = Form(...)):

    # TOOD: check user token if eligible
    if userDatabase.hasUserByLogonNme(username):
        userDatabase.updateContactEmail(username, displayname)
    pass
    