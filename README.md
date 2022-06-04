# FutureSQR
Code Review Tool - Future Source Quality Review

## Why another code review system?
Well, it caught me off guard, that Jetbrains decided to discontinue their product at the beginning of 2022. Upsource was used for a bunch 
of reasons - the main ones were: easy to use, easy to administrate and most importantly easy to integrate into a workflow.

Unlike many people believe, there are some good reasons to not only look at Git and their  feature-branch and pull-request system. 
Despite Git being a quite useful SCM system, people tend to forget, that there are other SCM systems out there for a reason. We tend to
forget, that Git was developed for a reason, namely to develop a monolithic system, in a distributed manner, where you don't trust the contributors,
while at the same time, to not discourage them to provide useful improvements to the code base. This is one undeniable strength of Git.
It solved a certain type and style of collaboration in an open source environment, while at the same time avoiding "contributions" of malicious contributors 
or actors. 

Another still important SCM system is SVN and there are good reasons to prefer it over other SCM systems. Like in cases where you combine
SCM with configuration management and want the configuration management also being version managed as well.

Therefore we can conclude there are good reasons to support Git as well as SVN for a code review tool. 

Another important thing is that there are different types of collaborations possible. In case of a corporate environment, there is an intrinsic
alignment between members of a team, which is done by the working contract. Therefore a SCM system which is enforcing some sort of 
synchronization process (which is useful in an open source environment) might be considered inefficient in a corporate environment. The
current most efficient type collaboration at development is trunk-based-development. You can find enough sources supporting this claim,
don't let me spoon feed you. In a corporate environment trunk-based-development is considered SOTA (state of the art). So now the big 
question, if trunk based development is more efficient compared to feature-branches, do we need to use a SCM supporting it, or is it just
a nice-to-have feature (remember in a corporate setting)?

Most of the newer code review tools only have the Git / Pull-Requests and the feature-branch development in mind. But this feature-branch approach
requires extra synchronization and creates extra bottlenecks and creates extra waiting times until a delivery of a feature or a bug fix. The
delivery of a feature or a bugfix becomes somehow unpredictable. 

There is a need for a code-review system which can still work post commit, which adds no extra synchronization between people and where
people are not forced to wait for each other. An improvement of the source code as a result of a code review can still be applied later on trunk
and can be delivered to the customer. There is no need to punish people by rejecting their code, in an environment where they are paid for
creating this code in the first place. When there is a need to improve the code, it can still be done. Each time the code is not integrated to trunk, the 
non integrated code looses value. The longer code waits for its integration on trunk, the more likely it is that errors post integration are found
late - even though it is reviewed.

Therefore a demand for a code-review tool what is able to handle at least SVN and GIT and what is supporting trunk-based development
at its core still exists. This project will hopefully provide a solution which is as easy to use, as the tool which I still regard highly despite being
discontinued. It still is and was a good, simple and efficient tool - Thank you for this.

Anyhow, as the world progresses, there is still demand for a good, simple and efficient code review tool. Let's make it!

## Current State of this Project

Attention: The implementation has not yet been started. Currently some basic ideas and white paper like documents are written down.

* Description of what we want to reach (some green/white paper like description) ((**The current phase**)) 
  * [You may want to have a look here first](FutureSQR-Architecture/documents/README.md)
  * It describes some of the objectives and some basic requirements
* Tryout Section
  * [Some concepts which I'm not familiar with, but deem somehow important](Tryouts/README.md)
* Work on requirements and user stories
  * Extract requirements and user stories from the description
* Work towards a first System and Software architecture
  * Identify architecturally relevant requirements
  * Identify quality attributes for the system
  * Identify a system architecture
  * Identify a software architecture
  * document the system and its software architecture
  * document the cross section concepts / cross cutting-concern and their particular architecture and solutions
* Iterate between architecture and requirements
* Specify the mode of development and split these concerns
  * e.g. separation of frontend and backend development, (e.g. by specifying the exchanged data)
  * e.g. how to see something from day #1 of development
  * e.g. write some scripts / tools for this mode of development
  * e.g. plan a mocked development back-end (maybe python with fast-api)

## Principles of Development

* Useful from Day One
* My three other principles
* Two Monoliths and a Mockup
  * Back-End-Monolith
  * Back-End-Mockup
  * Front-End-Monolith