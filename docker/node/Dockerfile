FROM node:8.10.0

RUN apt-get update && apt-get install -y git
RUN npm install pm2

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

EXPOSE 8081