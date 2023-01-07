# FutureSQR-TomcatDevBackend

This project is here to support the development of the java based backend components as long as there is 
no other "professional" backend available.

This project only works in this configuration with Java 1.8. So there might be some issues - but these are 
simply not important enough to solve right now. Also this is a using a vulnerable version of everything, but
for me it is sufficient enough, that this configuration works right now with the lowest amount of effort. 

**Do not put this code anywhere near to production.**

This tomcat instance will do it's job for a while until the real java backend development is started.

In the meantime, the other JAVA components (SCMAccess, Core, ReviewSystem-Core and others) will be developed.
These components are more or less independent of the architecture accessing these components. Therefore, they
can be developed with the simplest effort for a webserver in mind, and these components should not be dependent
on a certain webserver architecture.

## Basic Idea

We will replicate the basic developer python backend in java. But, why should we replicate all what was done 
again in java instead of keeping python? Well, I intended to use python for a rapid prototype development 
(a.k.a. tracer-bullet) see:  

* https://growsmethod.com/practices/TracerBullets.html 
* The Pragmatic Programmer (20th Anniversary Edition) - David Thomas, Andrew Hunt. in Chapter 2, Topic 12. 2020.

The python backend practically outlived it's usefulness to learn and to create a proof that a code review 
system is doable. Now we know more about the system, what needs to be done and how it can be done, such that 
we can develop the other core components in java and build the system from here. But despite the knowledge
gained for the java part, I still need a system with rapid feedback, cycle time, to re-develop the most
important core components first. If these are done, we will then re-assess, how to best make it a fast and 
secure web-application.

## Some More Information

I needed the python backend in the first place for a rapid development of the typescript/angular frontend - 
to get an idea, what is actually required from such a system. Think of an experimental and explorative 
prototype to identify the components and their interactions - with this a more educated guess can be made 
about the other components and their behavior.

## Running the backend

Run the command ```runBackend.bat```.

This URL should provide a hello world JSON entry: 

* http://localhost:8000/FutureSQR/rest/project/testme

After build and depolyment from the frontend project, you should be able to access this URL and see the login screen 

* http://localhost:8000/FutureSQR/


## Some Infos about the application - and why Tomcat 7 is a pain...

* the configured rewrite valve doesn't work because it is tomcat 7.0.49 it is also not available in 7.0.109...
* the valve is definitely avail in 8.5.9 (also in 8.0.8)
* so maybe i need a tomcat 8 runner ....

* why is that rewrite important?
** Well the angular web application has it's own parser for the file structure
** but if a user makes a request, the full url is sent to the webserver - since the path structure is unknown to the server it will answer with a 404 instead of serving the base index.html. Therefore deep url's into the applictaion will not end in the application but rather relayed to the server, which should only provide the index.html in the folder for the base application.