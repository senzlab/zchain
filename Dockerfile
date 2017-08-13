FROM ubuntu:16.04

MAINTAINER Eranga Bandara (erangaeb@gmail.com)

# install required packages
RUN apt-get update -y
RUN apt-get install -y python-software-properties
RUN apt-get install -y software-properties-common

# install java
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update -y
RUN apt-get install -y oracle-java8-installer
RUN rm -rf /var/lib/apt/lists/*
RUN rm -rf /var/cache/oracle-jdk8-installer

# set JAVA_HOME
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# set service variables
ENV SENZIE_NAME sampath
ENV SWITCH_HOST dev.localhost
ENV SWITCH_PORT 7070
ENV CASSANDRA_HOST dev.localhost
ENV CASSANDRA_PORT 9042
ENV CASSANDRA_KEYSPACE zchain

# working directory
WORKDIR /app

# copy file
ADD target/scala-2.11/zchain-assembly-1.0.jar zchain.jar

# logs volume
RUN mkdir logs
VOLUME ["/app/logs"]

# .keys volume
VOLUME ["/app/.keys"]

# command
ENTRYPOINT [ "java", "-jar", "/app/zchain.jar" ]

