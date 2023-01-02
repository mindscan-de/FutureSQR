# FutureSQR-ReviewSystem-Core

This project contains the domain design part of the review system. This project defines the language 
and concepts for the core code review system.

FutureSQR-ReviewSystem-Core is where we create the core domain objects, value objects and where
we perform calculations based on these objects. This domain model is then intended to be used 
in a different project. This domain code, doesn't take serializing and storing these objects into 
account, and doesn't handle database code and such. This is the responsibility of a different
layer of the system to persist this state in a database or even a cache or somewhere else. 
 
A web server will be implemented with dependencies on this project such that we can have a 
nice REST-backend instead of he pure python developer backend (fastapi) - what is there for 
developing the UI and the basic concepts.

In case of a web server, a web server must not serialize the instances of this class, but 
must provide an output model and a model-to-model transformation to interact with the UI. 
The web server responsibility is to manage the connections, convert and provide the data to 
the UI or other REST clients.

The reason why this domain model doesn't contain serialization or de-serialization is, to allow
for future selection of a proper database or for supporting different databases. The business
and cache logic for code reviews doesn't depend on a database.