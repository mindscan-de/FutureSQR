# FutureSQR-DevBackend

This is a development backend written in python using fastapi. This is one of many backends, 
which can communicate properly with the FutureSQR-Frontend to provide some mockup data to the
frontend or delivers particular scenarios.

Because we are using FastAPI we can actually write many small webservers or only one, to test
how the web-application behaves - without invoking the real backend. With that approach we 
can do rapid prototyping and development in python and after successfully implementing it and
figuring out missing data and such, its behavior can be reimplemented in the backend, which 
might be slower in development, because the backend will have to solve some more low level 
stuff, which can take some time until it works decently.

Because we know it works, it will at least be only developed once for the real backend. My 
guess for the real backend is, that I will probably implement it in Java. But the development
will be slightly slower in Java than in python. The DevBackend is just some proof of concept 
code - and helps to debug and solve the concepts and problems between FutureSQR-Frontend and 
its particular FutureSQR-Backend.



# TODO: Fast API offline 

use for doc and redoc folder - too much CDN use vs too much offline

* https://github.com/tiangolo/fastapi/issues/608
* https://fastapi.tiangolo.com/advanced/extending-openapi/#self-hosting-javascript-and-css-for-docs

* another way would be to use
* https://pypi.org/project/fastapi-offline/

# Noob Guide

If you touch Node.js first time in your life but want to set up a development environment anyway, read the following steps.

## Setup Development Environment

In order to install the required development environment on Windows please check the following steps.

* Install the Node.js from https://nodejs.org. In case of doubt it's safe to pick the LTS version.
* "Install additional Tools for Node.js" script you will find after Node.js installation in the folder Node.js of your Start menu in Windows. This helps you set up the recent environment.
* Next step is to start "Node.js command prompt" **with administrator permissions** to set up additional dependencies.
  * 'pip install python-multipart'
  * The command 'pip install "fastapi[all]"' sets up your fastapi and uvicorn. You can verify your success by run 'uvicorn' command. It shall exist now.

## Run The App

* It's recommended to call all commands from "Node.js command prompt". Administrator permissions are no more required here.
* All backends provide run commands and can be triggered immediately  after setup.
* The frontend tryouts need a call of 'npm install' in their working directories in advance to the run*.bat command. Frontend directories can be recognized by their package.json defining the Angular dependencies.

