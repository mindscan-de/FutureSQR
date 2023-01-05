# FutureSQR-TomcatDevWebServer

This project is here to support the development of the java based backend components as long as there is 
no other "professional" backend available. 

This Tomcat instance will do it's job for a while until the real java backend development is started.

In the meantime, the other JAVA components (SCMAccess, Core, ReviewSystem-Core and others) must be developed.
These components are more or less independent of the architecture accessing these components. Therefore they
can be developed with the simplest effort for a webserver in mind, and these components should not be dependent
on a certain webserver architecture.

## Basic Idea

Replicate the basic developer python backend as java. Why replicate all what i did again for java instead of
python? Well, I intended to use python for a rapid prototype development (a.k.a. tracer-bullet) see:  

* https://growsmethod.com/practices/TracerBullets.html 
* The Pragmatic Programmer (20th Anniversary Edition) - David Thomas, Andrew Hunt. in Chapter 2, Topic 12. 2020.

The python backend practically outlived it's usefulness to learn and proof that a code review system is 
possible. Now we know more about the system, what needs to be done and how it can be done, such that we 
can develop the other core components in java and build the system from here. But despite the knowledge
gained for the java part, I still need a system with rapid feedback, cycle time, to re-develop the most 
important core components first. If these are done, we will then re-assess, how to best make it a fast 
and secure web-application.

## Some More Information

I needed the python backend for a rapid develop of the typescript/angular frontend - to get an idea, what 
is actually required from such a system. Think of an experimental prototype to explore the components and
their interactions - with this a more educated guess can be made about the other components and their
behavior.