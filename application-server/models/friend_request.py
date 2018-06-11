from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.data_already_exists_exception import DataAlreadyExistsException
import bson
import time
import uuid


class FriendRequestModel():

	@staticmethod
	def get_friends_requests_rcv_by_user_id(user_id):
		friends_requests_user_id = []
		response = []
		data = {}
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		friends_requests_rcv = db.friends_request.find({'user_id_rcv': user_id})
		for doc in friends_requests_rcv:
			friends_requests_user_id.append(doc["user_id_sender"])
			data[doc["user_id_sender"]] = {
												"_id": str(doc["_id"]),
												"user_id": doc["user_id_sender"],
												"last_name": "",
												"name": "",
												"date": doc["date"]
											}

		friends_requests_data = db.users.find({'_id':{"$in":friends_requests_user_id}});
		for doc in friends_requests_data:
			data[str(doc["_id"])]["last_name"] = doc["last_name"]
			data[str(doc["_id"])]["name"] = doc["name"]

		return list(data.values())

	@staticmethod
	def get_friends_requests_sent_by_user_id(user_id):
		friends_requests_user_id = []
		response = []
		data = {}
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		friends_requests_rcv = db.friends_request.find({'user_id_sender': user_id})
		for doc in friends_requests_rcv:
			friends_requests_user_id.append(doc["user_id_rcv"])
			data[doc["user_id_rcv"]] = {
												"_id": str(doc["_id"]),
												"user_id": doc["user_id_rcv"],
												"last_name": "",
												"name": "",
												"date": doc["date"]
											}

		friends_requests_data = db.users.find({'_id':{"$in":friends_requests_user_id}});
		for doc in friends_requests_data:
			data[str(doc["_id"])]["last_name"] = doc["last_name"]
			data[str(doc["_id"])]["name"] = doc["name"]

		return list(data.values())

	@staticmethod
	def exists_request(request_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		request = db.friends_request.find_one({'_id': request_id})

		if request == None:
			raise NoDataFoundException

		return request_id

	@staticmethod
	def get_friend_request(request_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		friend_req = db.friends_request.find_one({'_id': request_id})
		friend_req['_id'] = str(friend_req['_id'])
		return friend_req

	@staticmethod
	def remove_friend_request(request_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		friend_req = db.friends_request.find_one({'_id': request_id})
		db.friends_request.remove({'_id': request_id})
		friend_req['_id'] = str(friend_req['_id'])
		return friend_req

	@staticmethod
	def create_friend_request(user_id_sender, user_id_rcv, msg, picture):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		request = db.friends_request.find_one({'user_id_sender': user_id_sender, 'user_id_rcv': user_id_rcv})

		if (request != None):
			raise DataAlreadyExistsException

		date = time.strftime("%d/%m/%Y", time.localtime())
		friend_req_id = str(uuid.uuid4().hex)
		db.friends_request.insert({	'_id': friend_req_id,
									'user_id_sender': user_id_sender,
									'user_id_rcv': user_id_rcv,
									'message': msg,
									'picture': picture,
									'date': date})

		friend_req = db.friends_request.find_one({'_id': friend_req_id})
		friend_req['_id'] = str(friend_req['_id'])
		return friend_req
