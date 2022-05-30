# FutureSQR - Tryouts

This is a directory containing small projects to figure things out. Either whether they are feasible, or
how they translate to a particular development mode and so on. Because i have to solve and figure out some
techniques previously unknown to me. 

One of the goals is to develop the front-end completely independent from the back-end, such both things can
be developed in parallel or in iterations. And i also want to have a kind of mocked back-end server, which can
be used instead of the real back-end, in case it is not finished yet, but the front-end-development should not
be impaired by that.

Before, I used to develop single page applications using angular. One of the tryouts will address the issue,
how an Angular Application can be developed in smaller parts without delivering the user hundreds of kilobytes
of javascript with a full application, and how to split it into different components/views to have smaller
scale partial apps. Then figure out how that can then be delivered to the user using a webserver and how the 
deployment would look like. e.g. in a tomcat based server.

Tryout_0x01

Angular app with individual components and some deeper routing, with some kind of include apps. And then 
figure out how a deployment and a build of such an app would look like (create a buildscript/and then also
check the dist-folder).

Tryout_0x02

Make a minimal tomcat based server, and check, whether the dist folder of tryout_0x01 can be used and will 
serve that application even with deeper url-structures. Maybe this will require some rewrite rules?
