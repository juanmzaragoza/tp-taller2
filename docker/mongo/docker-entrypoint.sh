#!/bin/bash
echo "Creacion de la Estructura Minima de la BD del AppServer"
mongo appserverdb --eval 'db.dropDatabase();'
mongo appserverdb --eval 'db.dropUser("app_user");'
mongo appserverdb --eval 'db.createUser({user:"app_user", pwd:"app_pass", "roles":["dbOwner"]});'

mongo appserverdb --eval 'profile = {"_id": ObjectId("5ae66a31d4ef925dac59a94b"), "_rev":"", "last_name" : "Gomez", "name" : "Pepe", "birthday" : "01/01/2000", "gender" : "M", "email" : "pepe@email.com", "fb_account" : "", "gmail_account" : "pepe@gmail.com", "profile_picture": "" };	
						  profile_id = db.profiles.insert(profile);
						  user = {"_id": ObjectId("5ae8ffff7cb6e634da74737b"), "_rev":"", "user": "root", "profile_id": "5ae66a31d4ef925dac59a94b", "created_time": "" , "last_login":""};
						  db.users.insert(user);
						  storie1= { "_id": ObjectId("5ae66a31d4ef925dac59a95b"), "_rev" : 0, "created_time" : 0, "updated_time" : 0, "title" : "First Storie", "description" : "Hello World! This is my first Storie!", "location" : "", "visibility" : "public", "multimedia" : "", "story_type" : "normal"};
						  storie1_id=db.stories.insert(storie1);
						  profiles_storie1 = {"profile_id": "5ae66a31d4ef925dac59a94b","storie_id":"5ae66a31d4ef925dac59a95b"};
						  db.profiles_stories.insert(profiles_storie1);
						  storie2= { "_id": ObjectId("5ae66a31d4ef925dac59a96b"), "_rev" : 0, "created_time" : 0, "updated_time" : 0, "title" : "Second Storie", "description" : "Hello World! This is my second Storie!", "location" : "", "visibility" : "public", "multimedia" : "", "story_type" : "normal" };
						  storie2_id=db.stories.insert(storie2);
						  profiles_storie2 = {"profile_id": "5ae66a31d4ef925dac59a94b","storie_id":"5ae66a31d4ef925dac59a96b"};
						  db.profiles_stories.insert(profiles_storie2);'
exec "$@";
