# FutureSQR-ReviewSystem-Core

This project contains the DDD-part of the ReviewSystem. This project defines the language and concepts
for the Review System.

Here is where we create the DomainObjects and perform calculations based on these Objects, so we can
use them in a different project to store these objects in a database, a cache on disk or somewhere 
else.

A web server will be implemented based on this project such that we can have a nice REST-backend instead
of a pure python developer backend - what is there for developing the UI and the concepts.

In case of a web server, a web server must not serialize the instances of this class, but 
must provide an output model and a model-to-model transformation to interact with the UI. 
The web server responsibility is to manage the connections, convert and provide the data to 
the UI or other REST clients.

The reason why this domain model doesn't contain serialization or de-serialization is, to allow
for future selection of a proper database or supporting different databases.