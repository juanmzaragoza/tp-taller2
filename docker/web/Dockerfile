FROM teracy/angular-cli

ENV HOME=/home/nodejs
ENV APP_NAME=web


# before switching to user we need to set permission properly

WORKDIR $HOME/$APP_NAME

RUN npm install

CMD ["ng", "serve", "--host", "0.0.0.0" ]