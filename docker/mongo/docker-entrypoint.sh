#!/bin/bash
echo "Creacion de la Estructura Minima de la BD del AppServer"
mongo appserverdb --eval 'db.dropDatabase();'
mongo appserverdb --eval 'db.dropUser("app_user");'
mongo appserverdb --eval 'db.createUser({user:"app_user", pwd:"app_pass", "roles":["dbOwner"]});'

mongo appserverdb --eval 'user1 = {"_id": "10", "_rev":"", "user_name": "userName1", "last_name" : "Gomez", "name" : "Pepe", "birthday" : "01/01/2000", "gender" : "M", "email" : "pepe@email.com", "picture": "" };
						  db.users.insert(user1);'

mongo appserverdb --eval 'user2 = {"_id": "4", "_rev":"", "user_name": "userName2", "last_name" : "Fernandez", "name" : "Maria", "birthday" : "01/01/1980", "gender" : "F", "email" : "maria@email.com", "picture": "" };
						  db.users.insert(user2);'

mongo appserverdb --eval  'user3 = {"_id": "2", "_rev":"", "user_name": "userName3","last_name" : "Fernandez", "name" : "Carlos", "birthday" : "01/01/1980", "gender" : "M", "email" : "carlos@email.com", "picture": "" };
						  db.users.insert(user3);'

mongo appserverdb --eval  'storie1= { "_id": "5ae66a31d4ef925dac59a95b", "_rev" : "", "created_time" : 0, "updated_time" : 0, "title" : "First Storie", "description" : "Hello World! This is my first Storie!", "location" : "", "visibility" : "public", "multimedia" : "", "story_type" : "normal"};
						  db.stories.insert(storie1);'

mongo appserverdb --eval  'user_storie1 = {"user_id": "10","storie_id":"5ae66a31d4ef925dac59a95b"};
						  db.users_stories.insert(user_storie1);'

mongo appserverdb --eval  'storie2= { "_id": "5ae66a31d4ef925dac59a96b", "_rev" : "", "created_time" : 0, "updated_time" : 0, "title" : "Second Storie", "description" : "Hello World! This is my second Storie!", "location" : "", "visibility" : "public", "multimedia" : "", "story_type" : "normal" };
						  db.stories.insert(storie2);'

mongo appserverdb --eval  'user_storie2 = {"user_id": "10","storie_id":"5ae66a31d4ef925dac59a96b"};
						  db.users_stories.insert(user_storie2);'

mongo appserverdb --eval  'storie3= { "_id": "5ae66a31d4ef925dac59a97b", "_rev" : "", "created_time" : 0, "updated_time" : 0, "title" : "Maria First Storie", "description" : "Hello World! I am Maria, this is my first Storie!", "location" : "", "visibility" : "public", "multimedia" : "", "story_type" : "normal" };
						  db.stories.insert(storie3);'

mongo appserverdb --eval  'user_storie3 = {"user_id": "4","storie_id":"5ae66a31d4ef925dac59a97b"};
						  db.users_stories.insert(user_storie3);'

mongo appserverdb --eval  'user_friend1 = {"user_id_sender": "10","user_id_rcv":"4", "date": "01/05/2018"};
						  db.friends.insert(user_friend1);'

mongo appserverdb --eval  'user_friend2 = {"user_id_sender": "10","user_id_rcv":"2", "date": "02/05/2018"};
						  db.friends.insert(user_friend2);'

mongo appserverdb --eval  'user_comment1 = {"user_id": "10","storie_id":"5ae66a31d4ef925dac59a97b", "message": "Comment 1", "date": "02/05/2018"};
						  db.storie_comments.insert(user_comment1);'

exec "$@";
