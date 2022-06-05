'''
Created on 05.06.2022

@author: JohnDoe
'''

from fastapi import FastAPI, Form, HTTPException

app = FastAPI()

@app.get("/")
def read_root():
    return {"message":"Hello World! It works! But now, go away!"}
