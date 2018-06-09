from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.data_version_exception import DataVersionException
from bson.objectid import ObjectId
import bson
import uuid

class UserDataModel():

	@staticmethod
	def get_user_data_by_user_id(user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		user = db.users.find_one({'_id': user_id})

		if user == None:
			raise NoDataFoundException

		return user

	@staticmethod
	def update_user_data_by_user_id(user_id, body):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		user = db.users.find_one({"_id": user_id})
		
		if user == None:
			raise NoDataFoundException
		
		if (user['_rev'] != body.get('_rev')):
			raise DataVersionException
		
		body['_rev'] = uuid.uuid4().hex
		del body['_id']
		user = db.users.find_and_modify({"_id": user_id}, {'$set': body })
		user = db.users.find_one({"_id": user_id})

		return user

	@staticmethod
	def exist_user(user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		user = db.users.find_one({'_id': user_id})

		if user == None:
			raise NoDataFoundException
	
	@staticmethod
	def insert_user(user_id, username):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		user = {
			'_id': user_id,
			'_rev': "",
			'user_name': username,
			'last_name': 'lastName '+ username,
			'name': 'name ' + username,
			'birthday': "10/05/2018",
			'gender': 'M',
			'email': username+'@email.com',
			'picture': ''}
		db.users.insert(user)
	
	@staticmethod
	def get_all_users_except(user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		users = db.users.find({ '_id': { '$ne': user_id } });
		response = {}
		c = 0
		for user in users:
			user_id = str(user['_id'])
			response[c] = {
							'_id': user_id,
							'last_name': user['last_name'],
							'name': user['name'],
							'picture': user['picture']
						}
			c += 1	
		
		return response
