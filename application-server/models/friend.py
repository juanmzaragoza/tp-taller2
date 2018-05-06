from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from errors_exceptions.no_data_found_exception import NoDataFoundException
from bson.objectid import ObjectId

class FriendModel():

	@staticmethod
	def get_friends_by_user_id(user_id):
		friends_user_id = []
		response = []
		data = {}
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		friends = db.friends.find({'user_id_sender': user_id})
		for doc in friends:
			friends_user_id.append(ObjectId(doc["user_id_rcv"]))
			data[doc["user_id_rcv"]] = {
												"user_id": doc["user_id_rcv"],
												"last_name": "",
												"name": "",
												"date": doc["date"]
											}
		
		friends = db.friends.find({'user_id_rcv': user_id})
		for doc in friends:
			friends_user_id.append(ObjectId(doc["user_id_sender"]))
			data[doc["user_id_sender"]] = {
													"user_id": doc["user_id_sender"],
													"last_name": "",
													"name": "",
													"date": doc["date"]
												}
		friends_data = db.users.find({'_id':{"$in":friends_user_id}});
		for doc in friends_data:
			data[str(doc["_id"])]["last_name"] = doc["last_name"]
			data[str(doc["_id"])]["name"] = doc["name"]

		return data.values()
