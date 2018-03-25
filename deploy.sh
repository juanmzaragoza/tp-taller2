#/bin/bash
#
# Consult credentials on Slack
#
# heroku-applicationserver	https://git.heroku.com/heroku-applicationserver.git
# heroku-sharedserver	https://git.heroku.com/heroku-sharedserver.git
#

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

printf "Deploying ${GREEN}application server${NC}...\n"
# set application-server user
heroku accounts:set application-server
# add applicationserver.git remote
heroku git:remote -a heroku-applicationserver
# rename heroku defaults
git remote rename heroku heroku-applicationserver
# push to server
git subtree push --prefix application-server heroku-applicationserver master
if [ $? -eq 0 ]; then
    printf "${GREEN}[OK]${NC} Deploying application server was successfully finish\n"
else
    printf "${RED}[ERROR]${NC} Deploying application server was finished with errors\n"
fi


printf "Deploying ${GREEN}shared server${NC}...\n"
# set application-server user
heroku accounts:set shared-server
# add applicationserver.git remote
heroku git:remote -a heroku-sharedserver
# rename heroku defaults
git remote rename heroku heroku-sharedserver
# push to server
git subtree push --prefix shared-server heroku-sharedserver master
if [ $? -eq 0 ]; then
    printf "${GREEN}[OK]${NC} Deploying shared server was successfully finish\n"
else
    printf "${RED}[ERROR]${NC} Deploying shared server was finished with errors\n"
fi

printf "Deploying ${GREEN}frontend web${NC}...\n"
# set web user
heroku accounts:set web
# add applicationserver.git remote
heroku git:remote -a heroku-storiesweb
# rename heroku defaults
git remote rename heroku heroku-storiesweb
# push to server
git subtree push --prefix web heroku-storiesweb master
if [ $? -eq 0 ]; then
    printf "${GREEN}[OK]${NC} Deploying frontend web was successfully finish\n"
else
    printf "${RED}[ERROR]${NC} Deploying shared server was finished with errors\n"
fi
