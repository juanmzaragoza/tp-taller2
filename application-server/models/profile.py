from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.data_version_exception import DataVersionException
from bson.objectid import ObjectId
import bson
import uuid

class ProfileModel():

	@staticmethod
	def get_profile_by_user_id(user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		if (bson.objectid.ObjectId.is_valid(user_id) == False):
			raise NoDataFoundException

		user = db.users.find_one({'_id': ObjectId(user_id)})

		if user == None:
			raise NoDataFoundException

		profile = db.profiles.find_one({'_id': ObjectId(user.get('profile_id'))})
		if profile == None:
			raise NoDataFoundException
		return profile

	@staticmethod
	def update_profile_by_user_id(user_id, body):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		if (bson.objectid.ObjectId.is_valid(user_id) == False):
			raise NoDataFoundException
		user = db.users.find_one({'_id': ObjectId(user_id)})

		if user == None:
			raise NoDataFoundException
		
		profile = db.profiles.find_one({"_id": ObjectId(user.get('profile_id'))})
		
		if profile == None:
			raise NoDataFoundException
		
		if (profile['_rev'] != body.get('_rev')):
			raise DataVersionException
		
		body['_rev'] = uuid.uuid4().hex
		profile = db.profiles.find_and_modify({"_id": ObjectId(user.get('profile_id'))}, {'$set': body })
		profile = db.profiles.find_one({"_id": ObjectId(user.get('profile_id'))})

		return profile
