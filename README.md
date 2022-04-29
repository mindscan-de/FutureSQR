# FutureSQR
Code Review Tool - Future Source Quality Review

## Why another code review system?
Well, it caught me off guard, that Jetbrains decided to discontinue their product at the beginning of 2022. We used Upsource for a bunch 
of reasons.

Unlike many people believe, there are some good reasons to not only look at Git and their  feature-branch and pull-request system. 
Despite Git being a quite useful SCM system, people tend to forget, that there are other SCM systems out there for a reason. We tend to
forget, that Git was developed for a reason, to develop a monolithic system, in a distributed manner, where you don't trust the contributors,
while at the same time, to not discourage them, to provide useful improvements to the code base. This is one undeniable strength of Git.
It solved a certain type and style of collaboration in an open source environment, while avoiding "contributions" of malicious contributors 
or actors. 

Another still useful SCM system is SVN and there are good reasons to prefer it over other SCM systems. Like in cases where you combine
SCM with configuration management and want the configuration management also being version managed as well.

Therefore we can conclude there are good reasons to support Git as well as SVN for a code review tool. 

Another important thing is that there are different types of collaborations possible. In case of a corporate environment, there is an intrinsic
alignment between members of a team, which is done by the working contract. Therefore a SCM system which is enforcing some sort of 
synchronization process (which is useful in an open source environment) might be considered inefficient in a corporate environment. The
current most efficient type collaboration at development is trunk-based-development. You can find enough sources supporting this claim,
don't let me spoon feed you. In a corporate environment trunk-based-development is considered SOTA (state of the art). So now the big 
question, if trunk based development is more efficient compared to feature-branches, do we need to use a SCM supporting it, or is it just
a nice-to-have feature (remember in a corporate setting)?

Most of the newer code review tools only have the Git / Pull-Requests and the feature-branch development in mind. But this approach
requires extra synchronization and creates extra bottlenecks and creates extra waiting times until the delivery of a feature or a bug fix. The
delivery of a feature or a bugfix becomes somehow unpredictable.

There is a need for a code-review system which can still work post commit, which adds no extra synchronization between people and where
people are not forced to wait for each other. An improvent of the source code as a result of a code review can still be applied later on trunk
and can be delivered to the customer. There is no need to punish people by rejecting their code, in an environment where they are paid for
creating this code in the first place. When there is a need to improve the code, it can still be done. Each time the code is not integrated, the 
non integrated code looses value. The longer code waits for its integration on trunk, the more likely it is that errors post integration are found
late - even though it is reviewed. 

Therefore the demand for a code-review tool which able to handle at least SVN and GIT and which is supporting trunk-based development
as its core still exists. This project will hopefully provide a solution which is as easy to use, as the tool which I still regard highly despite being
discontinued. It still is and was a good, simple and efficient tool - Thank you for this.

Anyhow, as the world progresses, there is still demand for a  good, simple and efficient code review tool. Let's make it!
