FROM ubuntu:16.04

RUN apt-get update 
RUN apt-get install -y sudo && rm -rf /var/lib/apt/lists/*
RUN apt-get update  && apt-get install -y software-properties-common 
RUN apt-get install -y curl
RUN apt-get install -y apt-transport-https

RUN sudo add-apt-repository "deb https://cli-assets.heroku.com/branches/stable/apt ./"
RUN curl -L https://cli-assets.heroku.com/apt/release.key | sudo apt-key add -
RUN sudo apt-get update
RUN sudo apt-get install heroku

RUN heroku plugins:install heroku-accounts

RUN mkdir -p /usr/src/shared-server
RUN mkdir -p /usr/src/application-server
RUN mkdir -p /usr/src/web

WORKDIR /usr/src