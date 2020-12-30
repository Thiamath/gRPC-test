# gRPC-test

Template project to test gRPC over java and investigate the best ways to architect and deploy.

## Intro

[gRPC](https://grpc.io) is a high performance Remote Procedure Call protocol that enables secure binary communication
between services. It is a nice approach to microservice communications and much better that REST in some scenarios.

## Tools used

<img alt="Gradle logo" src="https://gradle.com/wp-content/themes/fuel/assets/img/branding/gradle-elephant-icon-gradient.svg" width=30px height=30px>
Here I'll be using [gradle](https://gradle.org/) for dependency management, compilation, and tooling.

<img alt="JFrog logo" src="https://media.jfrog.com/wp-content/uploads/2017/12/20132914/Jfrog.png.webp" width=30px height=30px>
The proto artifact is deployed over [JFrog Artifactory](https://jfrog.com/artifactory/) using its
[gradle plugin](https://www.jfrog.com/confluence/display/JFROG/Gradle+Artifactory+Plugin). JFrog includes a free-tier
cloud service that allows some artifacts to be served for free.
