#!/bin/bash
echo "Creacion de la Estructura Minima de la BD del AppServer"
mongo appserverdb --eval 'db.dropDatabase();'
mongo appserverdb --eval 'db.dropUser("app_user");'
mongo appserverdb --eval 'db.createUser({user:"app_user", pwd:"app_pass", "roles":["dbOwner"]});'

mongo appserverdb --eval 'user1 = {"_id": "10", "_rev":"", "user_name": "userName1", "last_name" : "fb", "name" : "Nico", "birthday" : "01/01/2000", "gender" : "M", "email" : "pepe@email.com", "picture": "" };
						  db.users.insert(user1);'

mongo appserverdb --eval 'user2 = {"_id": "4", "_rev":"", "user_name": "userName2", "last_name" : "Masr", "name" : "Mevk", "birthday" : "01/01/1980", "gender" : "F", "email" : "maria@email.com", "picture": "" };
						  db.users.insert(user2);'

mongo appserverdb --eval  'user3 = {"_id": "2", "_rev":"", "user_name": "userName3","last_name" : "Zar", "name" : "Juanma", "birthday" : "01/01/1980", "gender" : "M", "email" : "carlos@email.com", "picture": "" };
						  db.users.insert(user3);'

mongo appserverdb --eval  'storie1= { "_id": "5ae66a31d4ef925dac59a95b", "_rev" : "", "created_time" : "16/04/2018 15:00:14", "updated_time" : "", "title" : "First Storie", "description" : "Hello World! This is my first Storie!", "location" : "", "visibility" : "public", "multimedia" : "", "story_type" : "normal"};
						  db.stories.insert(storie1);'

mongo appserverdb --eval  'user_storie1 = {"_id": "5ae66a31d4ef925dac77a95b", "user_id": "10","storie_id":"5ae66a31d4ef925dac59a95b"};
						  db.users_stories.insert(user_storie1);'

mongo appserverdb --eval  'storie2= { "_id": "5ae66a31d4ef925dac59a96b", "_rev" : "", "created_time" : "16/04/2018 15:00:14", "updated_time" : "", "title" : "Second Storie", "description" : "Hello World! This is my second Storie!", "location" : "", "visibility" : "public", "multimedia" : "", "story_type" : "normal" };
						  db.stories.insert(storie2);'

mongo appserverdb --eval  'user_storie2 = {"_id": "5ae66a31d4ef925dac59cccb", "user_id": "10","storie_id":"5ae66a31d4ef925dac59a96b"};
						  db.users_stories.insert(user_storie2);'

mongo appserverdb --eval  'storie3= { "_id": "5ae66a31d4ef925dac59a97b", "_rev" : "", "created_time" : "16/04/2018 15:00:14", "updated_time" : "", "title" : "Maria First Storie", "description" : "Hello World! I am Maria, this is my first Storie!", "location" : "", "visibility" : "public", "multimedia" : "", "story_type" : "normal" };
						  db.stories.insert(storie3);'

mongo appserverdb --eval  'user_storie3 = {"_id": "5ae66a31d4ef92588859a95b", "user_id": "4","storie_id":"5ae66a31d4ef925dac59a97b"};
						  db.users_stories.insert(user_storie3);'

mongo appserverdb --eval  'user_friend1 = {"_id": "5ae66fc1d4ef925dac59a95b", "user_id_sender": "10","user_id_rcv":"4", "date": "16/04/2018 15:00:14"};
						  db.friends.insert(user_friend1);'

mongo appserverdb --eval  'user_friend2 = {"_id": "5be66a31d4ef925dac59a95b", "user_id_sender": "10","user_id_rcv":"2", "date": "16/04/2018 15:00:14"};
						  db.friends.insert(user_friend2);'

mongo appserverdb --eval  'user_friend_request1 = {"_id": "6ae66a31d4ef925dac59a95b", "user_id_sender": "4","user_id_rcv":"2", "message": "Hello ...", "date": "16/04/2018 15:00:14"};
						  db.friends_request.insert(user_friend_request1);'

mongo appserverdb --eval  'user_comment1 = {"_id": "8ae66a31d4ef925dac59a95b", "_rev" : "", "user_id": "10","storie_id":"5ae66a31d4ef925dac59a97b", "message": "Comment 1", "date": "10/06/2018 17:13:33"};
						  db.storie_comments.insert(user_comment1);'

mongo appserverdb --eval  'user_reaction1 = {"_id": "8ae66a31d4ef925dac88a90a", "_rev" : "", "user_id": "10","storie_id":"5ae66a31d4ef925dac59a97b", "reaction": "LIKE", "date": "10/06/2018 17:13:33"};
						  db.storie_reactions.insert(user_reaction1);'

mongo appserverdb --eval 'user4 = {"_id": "1", "_rev":"", "user_name": "userName4", "last_name" : "Pepe", "name" : "Erik", "birthday" : "01/01/2000", "gender" : "M", "email" : "pepe@email.com", "picture": "" };
						  db.users.insert(user4);'

mongo appserverdb --eval  'storie4= { "_id": "5ae66a31d4ef925dac69a95b", "_rev" : "", "created_time" : "16/04/2018 15:00:14", "updated_time" : "", "title" : "First Storie", "description" : "Hello World! This is my first Storie!", "location" : "", "visibility" : "public", "multimedia" : "", "story_type" : "normal"};
						  db.stories.insert(storie4);'

mongo appserverdb --eval  'user_storie4 = {"_id": "5ae66a31d4e8925dac77a95b", "user_id": "1","storie_id":"5ae66a31d4ef925dac69a95b"};
						  db.users_stories.insert(user_storie4);'


mongo appserverdb --eval 'user5 = {"_id": "3", "_rev":"", "user_name": "userName5", "last_name" : "Dom", "name" : "Nico", "birthday" : "01/01/2000", "gender" : "M", "email" : "pepe@email.com", "picture": "" };
						  db.users.insert(user5);'

mongo appserverdb --eval  'storie5= { "_id": "5ae66a31d5ef925dac69a95b", "_rev" : "", "created_time" : "16/04/2018 15:00:14", "updated_time" : "", "title" : "First Storie", "description" : "Hello World! This is my first Storie!", "location" : "", "visibility" : "public", "multimedia" : "", "story_type" : "normal"};
						  db.stories.insert(storie5);'

mongo appserverdb --eval  'user_storie5 = {"_id": "5ae66a31d5ef925dac77a95b", "user_id": "3","storie_id":"5ae66a31d5ef925dac69a95b"};
						  db.users_stories.insert(user_storie5);'

mongo appserverdb --eval 'user6 = {"_id": "5", "_rev":"", "user_name": "userName6", "last_name" : "heanna", "name" : "kaoru", "birthday" : "01/01/2000", "gender" : "M", "email" : "pepe@email.com", "picture": "" };
						  db.users.insert(user6);'

mongo appserverdb --eval  'storie6= { "_id": "5ae66a31d6ef925dac69a95b", "_rev" : "", "created_time" : "16/04/2018 15:00:14", "updated_time" : "", "title" : "First Storie", "description" : "Hello World! This is my first Storie!", "location" : "", "visibility" : "public", "multimedia" : "", "story_type" : "normal"};
						  db.stories.insert(storie6);'

mongo appserverdb --eval  'user_storie6 = {"_id": "5ae66a31d6ef925dac77a95b", "user_id": "5","storie_id":"5ae66a31d6ef925dac69a95b"};
						  db.users_stories.insert(user_storie6);'


mongo appserverdb --eval  'user_friend3 = {"_id": "5ae66fc1d4ef925dar59a95b", "user_id_sender": "10","user_id_rcv":"1", "date": "16/04/2018 15:00:14"};
						  db.friends.insert(user_friend3);'

mongo appserverdb --eval  'user_friend4 = {"_id": "5be66a31d4ef925daq59a95b", "user_id_sender": "10","user_id_rcv":"3", "date": "16/04/2018 15:00:14"};
						  db.friends.insert(user_friend4);'

mongo appserverdb --eval  'user_friend5 = {"_id": "5be66a38d4ef925daq59a95b", "user_id_sender": "10","user_id_rcv":"5", "date": "16/04/2018 15:00:14"};
						  db.friends.insert(user_friend5);'

exec "$@";
