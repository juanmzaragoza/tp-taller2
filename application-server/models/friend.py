import uuid
from models.user_data import UserDataModel
from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.date_controller import DateController
from errors_exceptions.no_friend_found_exception import NoFriendFoundException
from errors_exceptions.friendship_already_exists_exception import FriendshipAlreadyExistsException

class FriendModel():
	'''
	@staticmethod
	def get_friends_by_user_id(user_id):
		data = {}
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		friends = db.friends.find({'user_id_sender': user_id})
		
		opt1 = {'user_id_sender': user_id}
		opt2 = {'user_id_rcv': user_id}
		friends = db.friends.find({ "$or": [ opt1, opt2 ]})
		
		for friend in friends:
			user_id_rcv = friend['user_id_rcv']
			user_id_sender = friend['user_id_sender']
			friend_user_id = user_id_rcv if (user_id == user_id_sender) else user_id_sender
			
			data[friend_user_id] = UserDataModel.get_user_reduced_data_by_user_id(friend_user_id)
			data[friend_user_id]["user_id"] = data[friend_user_id].pop("_id")
			data[friend_user_id]["_id"] = friend["_id"]
			data[friend_user_id]["date"] = DateController.get_date_time_with_format(friend["date"])

		return data.values()
	'''
	@staticmethod
	def get_friends_array_by_user_id(user_id):
		data = []
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		opt1 = {'user_id_sender': user_id}
		opt2 = {'user_id_rcv': user_id}
		friends = db.friends.find({ "$or": [ opt1, opt2 ]})
		
		for friend in friends:
			user_id_rcv = friend['user_id_rcv']
			user_id_sender = friend['user_id_sender']
			friend_user_id = user_id_rcv if (user_id == user_id_sender) else user_id_sender
			data.append(friend_user_id)
			
		return data
		
	@staticmethod
	def get_friends_by_user_id(user_id):
		data = []
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		opt1 = {'user_id_sender': user_id}
		opt2 = {'user_id_rcv': user_id}
		friends = db.friends.find({ "$or": [ opt1, opt2 ]})
		
		for friend in friends:
			user_id_rcv = friend['user_id_rcv']
			user_id_sender = friend['user_id_sender']
			friend_user_id = user_id_rcv if (user_id == user_id_sender) else user_id_sender
			friend.pop("user_id_rcv")
			friend.pop("user_id_sender")
			friend["user_id"] = friend_user_id
			friend["date"] = DateController.get_date_time_with_format(friend["date"])
			friend_with_user_data = FriendModel.get_friend_with_user_data(friend, friend["user_id"])
			data.append(friend_with_user_data)
			

		return data
		
	@staticmethod
	def get_friend_with_user_data(friend, user_id):
		user_data = UserDataModel.get_user_reduced_data_by_user_id(user_id)
		friend_with_user_data = {**user_data, **friend}
		return friend_with_user_data

	@staticmethod
	def create_friend(friend):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
				
		friend_id = str(uuid.uuid4().hex)
		user_id_sender = friend['user_id_sender']
		user_id_rcv = friend['user_id_rcv']
		date = DateController.get_date_time()
		friendship_exists = FriendModel.friendship_exists(user_id_sender, user_id_rcv)
		
		if friendship_exists == True:
			raise FriendshipAlreadyExistsException
			
		friend = FriendModel.get_new_friend(friend_id, user_id_sender, user_id_rcv, date)
		db.friends.insert(friend)
		
		friend["date"] = DateController.get_date_time_with_format(friend["date"])
		return friend
	
	@staticmethod
	def friendship_exists(user_id_sender, user_id_rcv):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		opt1 = {'user_id_sender': user_id_sender, 'user_id_rcv': user_id_rcv}
		opt2 = {'user_id_sender': user_id_rcv, 'user_id_rcv': user_id_sender}
		friendship = db.friends.find_one({ "$or": [ opt1, opt2 ]})
		
		if friendship == None:
			return False
		
		return True
	
	@staticmethod
	def delete_friend(friend_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		
		response = db.friends.find_one({'_id': friend_id})

		if response == None:
			raise NoFriendFoundException

		db.friends.remove({'_id': friend_id})
		response["date"] = DateController.get_date_time_with_format(response["date"])
		return response
		
	@staticmethod
	def get_new_friend(friend_id, user_id_sender, user_id_rcv, date):
		return {
			"_id": friend_id,
			"user_id_sender": user_id_sender,
			"user_id_rcv": user_id_rcv,
			"date": date
		}

	@staticmethod
	def count_today_friends():
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		date_from = DateController.today()
		date_to = DateController.tomorrow()
		count = db.friends.find({
			"$and" : [
				{ "date" : {'$gte': date_from} },
				{ "date" : {'$lt': date_to} }
			]
		}).count()
		return count
