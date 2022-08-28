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
def postLoginData(username:str = Form(...), password:str = Form(...)):
    # TODO: find user by uername in user passwd database
    # verify password,
    # either we generate a valid user dataset response
    # or we handle the wrong password by how?
    pwEntry = getPasswdEntry(username)
    if pwEntry is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="No such user or not authenticated"
            )
    
    return {
        'id': username
        }

@app.post("/FutureSQR/rest/user/add")
def addNewUser(
        logonname:str = Form(...), 
        password:str = Form(...), 
        displayname:str = Form(...),
        contactemail:str = Form(...)):
    if userDatabase.hasUserByLogonNme(logonname):
        return False
    
    userrow = userDatabase.insertNewUser(logonname, displayname, contactemail)
    
    # TODO: assign password to this user.
    pass

