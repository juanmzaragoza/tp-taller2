#/bin/bash
#
# Consult credentials on Slack
#
# heroku-applicationserver	https://git.heroku.com/heroku-applicationserver.git
# heroku-sharedserver	https://git.heroku.com/heroku-sharedserver.git
#

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
    printf "${GREEN}[ERROR]${NC} Deploying application server was finished with errors\n"
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
    printf "${GREEN}[OK]${NC} Deploy shared server terminado\n"
else
    printf "${GREEN}[ERROR]${NC} Deploying shared server was finished with errors\n"
fi
