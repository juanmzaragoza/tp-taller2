import uuid
from models.friend import FriendModel
from models.user_data import UserDataModel
from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.date_controller import DateController
from errors_exceptions.data_already_exists_exception import DataAlreadyExistsException
from errors_exceptions.no_friend_request_found_exception import NoFriendRequestFoundException
from errors_exceptions.friendship_already_exists_exception import FriendshipAlreadyExistsException
from errors_exceptions.friend_request_already_exists_exception import FriendRequestAlreadyExistsException

class FriendRequestModel():

	@staticmethod
	def get_friends_requests_rcv_by_user_id(user_id):
		data = {}
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		friends_requests_rcv = db.friends_request.find({'user_id_rcv': user_id})
		
		for freq in friends_requests_rcv:
			friend_req_id = freq["user_id_sender"]
			data[friend_req_id] = UserDataModel.get_user_reduced_data_by_user_id(friend_req_id)
			data[friend_req_id]["user_id"] = data[friend_req_id].pop("_id")
			data[friend_req_id]["_id"] = freq["_id"]
			data[friend_req_id]["date"] = DateController.get_date_time_with_format(freq["date"])
			data[friend_req_id]["message"] = freq["message"]
		
		return list(data.values())
		
	@staticmethod
	def get_friends_requests_sent_by_user_id(user_id):
		data = {}
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		friends_requests_sent = db.friends_request.find({"user_id_sender": user_id})
		
		for freq in friends_requests_sent:
			friend_req_id = freq["user_id_rcv"]
			data[friend_req_id] = UserDataModel.get_user_reduced_data_by_user_id(friend_req_id)
			data[friend_req_id]["user_id"] = data[friend_req_id].pop("_id")
			data[friend_req_id]["_id"] = freq["_id"]
			data[friend_req_id]["date"] = DateController.get_date_time_with_format(freq["date"])
			data[friend_req_id]["message"] = freq["message"]
		
		return list(data.values())

	@staticmethod
	def exists_request(request_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		request = db.friends_request.find_one({'_id': request_id})

		if request == None:
			raise NoFriendRequestFoundException
			
		request["date"] = DateController.get_date_time_with_format(request["date"])
		return request
	
	@staticmethod
	def get_friend_request(request_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		friend_req = db.friends_request.find_one({'_id': request_id})
		
		if friend_req == None:
			raise NoFriendRequestFoundException
		
		friend_req["date"] = DateController.get_date_time_with_format(friend_req["date"])
		return friend_req

	@staticmethod
	def remove_friend_request(request_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		friend_req = db.friends_request.find_one({'_id': request_id})

		if friend_req == None:
			raise NoFriendRequestFoundException
		
		db.friends_request.remove({'_id': request_id})
		friend_req["date"] = DateController.get_date_time_with_format(friend_req["date"])
		return friend_req
	
	@staticmethod
	def create_friend_request(user_id_sender, user_id_rcv, msg, picture):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		friend_request_exists = FriendRequestModel.friend_request_exists(user_id_sender, user_id_rcv)
		
		if (friend_request_exists == True):
			raise FriendRequestAlreadyExistsException
		
		friendship_exists = FriendModel.friendship_exists(user_id_sender, user_id_rcv)
		
		if friendship_exists == True:
			raise FriendshipAlreadyExistsException
		
		
		friend_req_id = str(uuid.uuid4().hex)
		date = DateController.get_date_time()

		friend_request = FriendRequestModel.get_new_friend_request(friend_req_id, user_id_sender, user_id_rcv, msg, date, picture)
		
		db.friends_request.insert(friend_request)
		friend_request["date"] = DateController.get_date_time_with_format(friend_request["date"])
		return friend_request
	
	@staticmethod
	def friend_request_exists(user_id_sender, user_id_rcv):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		opt1 = {'user_id_sender': user_id_sender, 'user_id_rcv': user_id_rcv}
		opt2 = {'user_id_sender': user_id_rcv, 'user_id_rcv': user_id_sender}
		friendship = db.friends_request.find_one({ "$or": [ opt1, opt2 ]})
		
		if friendship == None:
			return False
		
		return True
		
	@staticmethod
	def get_new_friend_request(friend_req_id, user_id_sender, user_id_rcv, message, date, picture):
		return {
			"_id": friend_req_id,
			"user_id_sender": user_id_sender,
			"user_id_rcv": user_id_rcv,
			"message": message,
			"picture": picture,
			"date": date
		}
