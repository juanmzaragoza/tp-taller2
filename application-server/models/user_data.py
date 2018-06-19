import uuid
from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.date_controller import DateController
from errors_exceptions.data_version_exception import DataVersionException
from errors_exceptions.no_user_data_found_exception import NoUserDataFoundException

class UserDataModel():

	@staticmethod
	def get_user_data_by_user_id(user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		user = db.users.find_one({'_id': user_id})
		
		if user == None:
			raise NoUserDataFoundException

		return user

	@staticmethod
	def update_user_data_by_user_id(user_id, body):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		user = db.users.find_one({"_id": user_id})
		
		if user == None:
			raise NoUserDataFoundException
		
		if (user['_rev'] != body.get('_rev')):
			raise DataVersionException
		
		user_id = body["_id"]
		rev = str(uuid.uuid4().hex)
		user_name = user["user_name"]
		last_name = body["last_name"]
		name = body["name"]
		birthday = body["birthday"] if ("birthday" in body) else "" 
		gender = body["gender"] if ("gender" in body) else "" 
		email = body["email"]
		pic = body["picture"]
			
		user = UserDataModel.get_new_user(user_id, rev, user_name, last_name, name, birthday, gender, email, pic)
		
		del user['_id']
		
		user = db.users.find_and_modify({"_id": user_id}, {'$set': user })
		user = db.users.find_one({"_id": user_id})

		return user

	@staticmethod
	def exist_user(user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		user = db.users.find_one({"_id": user_id})

		if user == None:
			raise NoUserDataFoundException
		
		return user
	
	@staticmethod
	def insert_user(user_id, user_name):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		rev = ""
		last_name = "lastName " + user_name
		name = "name " + user_name
		birthday = ""
		gender = ""
		email = user_name+"@email.com"
		pic = ""
				
		user = UserDataModel.get_new_user(user_id, rev, user_name, last_name, name, birthday, gender, email, pic)
		
		db.users.insert(user)
		
		return user
		
	@staticmethod
	def get_all_users_except(user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		UserDataModel.exist_user(user_id)
		users = db.users.find({ "_id": { "$ne": user_id } });
		response = []
		
		for user in users:
			user_id = user["_id"]
			last_name = user["last_name"]
			user_name = user["user_name"]
			name = user["name"]
			pic = user["picture"]
			
			response.append(UserDataModel.get_reduced_data_user(user_id, user_name, last_name, name, pic))
		
		return response
	
	@staticmethod
	def get_user_reduced_data_by_user_id(user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		user = db.users.find_one({'_id': user_id})
		
		if user == None:
			raise NoUserDataFoundException
		
		user_id = user["_id"]
		user_name = user["user_name"]
		last_name = user["last_name"]
		name = user["name"]
		pic = user["picture"]
			
		reduced_user_data = UserDataModel.get_reduced_data_user(user_id, user_name, last_name, name, pic)
		
		return reduced_user_data
		
	@staticmethod
	def get_new_user(user_id, rev, user_name, last_name, name, birthday, gender, email, pic):
		return {
			"_id": user_id,
			"_rev": rev,
			"user_name": user_name,
			"last_name": last_name,
			"name": name,
			"birthday": birthday,
			"gender": gender,
			"email": email,
			"picture": pic
		}
	
	@staticmethod
	def get_reduced_data_user(user_id, user_name, last_name, name, pic):
		return {
			"_id": user_id,
			"user_name": user_name,
			"last_name": last_name,
			"name": name,
			"picture": pic
		}
