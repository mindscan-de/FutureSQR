# FutureSQR-ReviewSystem-Core

This Project contains the DDD-Part of the ReviewSystem. This project defines the language and concepts
for the Review System.

Here is where we create the DomainObjects and perform calculations based on these Objects, so we can
use them in a different project to store these objects in a database, a cache on disk or somewhere 
else.

A webserver will be implemented based on this project such that we can have a nice REST-Backend instead
of a pure python developer backend - what is there for developing the UI and the concepts.

In case of the webserver, the webserver must not serialize the instances of this class, but 
must provide an output model and a model to model transformation to interact with the UI. 
The Web-Servers responsibility is to manage the connections, convert and provide the data to 
the UI or other REST clients.

The reason why this Domain model doesn't contain serialization or deserialization is to, allow
for future selection of a database or supporting differernt databases.