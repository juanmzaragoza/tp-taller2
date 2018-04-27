#!/bin/bash
echo "Creacion de la Estructura Minima de la BD del AppServer"
mongo appserverdb --eval 'db.dropDatabase();'
mongo appserverdb --eval 'db.createUser({user:"app_user", pwd:"app_pass", "roles":["dbOwner"]});'
mongo appserverdb --eval 'db.users.insert({user:"root"});'
mongo appserverdb --eval 'db.profiles.insert({user_id:1, last_name:"Gomez", name:"Pepe", birthday:"01/01/2000", gender:"M", email:"pepe@email.com", fb_account:"", gmail_account:"pepe@gmail.com"});'

exec "$@";
