# FutureSQR-DevBackend

This is a development backend written in python using fastapi. This is one of many backends, 
which can communicate properly with the FutureSQR-Frontend to provide some mockup data to the
frontend or delivers particular scenarios.

Because we are using FastAPI we can actually write many small webservers or only one, to test
how the web-application behaves - without invoking the real backend. With that approach we 
can do rapid prototyping and development in python and after sucessfully implementing it and
figuring out missing data and such, its behavior can be reimplemented in the backend, which 
might be slower in development, because the backend will have to solve some more lowlevel 
stuff, which can take som time until it works decently.

Because we know it works, it will at least be only developed once for the real backend. My 
guess for the real backend is, that I will probably implement it in Java. But the development
will be slightly slower in Java than in python. The DevBackend is just some proof of concept 
code - and helps to debug and solve the concepts and problems between FutureSQR-Frontend and 
its particular FutureSQR-Backend.



# TODO: Fast API offline 

use for doc and redoc folder - too much cdn use vs too much offline

* https://github.com/tiangolo/fastapi/issues/608
* https://fastapi.tiangolo.com/advanced/extending-openapi/#self-hosting-javascript-and-css-for-docs

* another way would be to use
* https://pypi.org/project/fastapi-offline/