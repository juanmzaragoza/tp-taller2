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
		if (bson.objectid.ObjectId.is_valid(user_id) == False):
			raise NoDataFoundException

		user = db.users.find_one({'_id': ObjectId(user_id)})

		if user == None:
			raise NoDataFoundException

		return user

	@staticmethod
	def update_user_data_by_user_id(user_id, body):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		if (bson.objectid.ObjectId.is_valid(user_id) == False):
			raise NoDataFoundException
		
		user = db.users.find_one({"_id": ObjectId(user_id)})
		
		if user == None:
			raise NoDataFoundException
		
		if (user['_rev'] != body.get('_rev')):
			raise DataVersionException
		
		body['_rev'] = uuid.uuid4().hex
		user = db.users.find_and_modify({"_id": ObjectId(user_id)}, {'$set': body })
		user = db.users.find_one({"_id": ObjectId(user_id)})

		return user

	@staticmethod
	def exist_user(user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		if (bson.objectid.ObjectId.is_valid(user_id) == False):
			raise NoDataFoundException

		user = db.users.find_one({'_id': ObjectId(user_id)})

		if user == None:
			raise NoDataFoundException
