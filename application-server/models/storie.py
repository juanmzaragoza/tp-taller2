import uuid
import pymongo
from models.comment import CommentModel
from models.reaction import ReactionModel
from models.user_data import UserDataModel
from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.date_controller import DateController
from errors_exceptions.data_version_exception import DataVersionException
from errors_exceptions.no_storie_found_exception import NoStorieFoundException

class StorieModel:
	
	@staticmethod
	def storie_exists(storie_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		storie = db.stories.find_one({"_id": storie_id})
		
		if storie == None:
			return False
		
		return True
		
	@staticmethod
	def create_user_storie(body):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		storie_id = str(uuid.uuid4().hex)
		created_time = DateController.get_date_time()
		updated_time = ""
		rev = ""
		title = body['title']
		desc = body["description"] if ("description" in body) else ""
		location = body['location']
		visibility = body['visibility']
		mult = body['multimedia']
		story_type = body['story_type']
		user_id = body['user_id']	
		storie = StorieModel.get_new_storie(storie_id, rev, created_time, updated_time, title, desc, location, visibility, mult, story_type)

		#storie_id = db.stories.insert(storie)
		db.stories.insert(storie)
		db.users_stories.insert({'user_id': user_id, 'storie_id': storie_id})
		
		storie['user_id'] = user_id
		storie['created_time'] = str(storie['created_time'])
		storie['updated_time'] = str(storie['updated_time'])
		
		return storie

	@staticmethod
	def update_storie(storie_id, body):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)

		act_storie = db.stories.find_one({'_id':  storie_id})

		if act_storie == None:
			raise NoStorieFoundException

		if act_storie['_rev'] != body.get('_rev'):
			raise DataVersionException

		rev = str(uuid.uuid4().hex)
		created_time = act_storie["created_time"] 
		updated_time = DateController.get_date_time()
		title = body["title"]
		desc = body["description"] if ("description" in body) else "" 
		location = body["location"]
		visibility = body["visibility"]
		mult = body["multimedia"]
		story_type = body["story_type"]
		
		storie = StorieModel.get_new_storie(storie_id, rev, created_time, updated_time, title, desc, location, visibility, mult, story_type)

		del storie['_id']
		
		res = db.stories.find_and_modify({'_id': storie_id},{'$set': storie})
		#storie = db.stories.find_one({'_id': storie_id})
		res['created_time'] = str(res['created_time'])
		res['updated_time'] = str(res['updated_time'])
		return res

	@staticmethod
	def get_stories(user_id):
		#data = {}
		data = []
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		
		stories = db.stories.aggregate([
										   {
											  "$lookup":
												 {
													"from": "users_stories",
													"localField": "_id",
													"foreignField": "storie_id",
													"as": "users_storie"
												}
										   },
										   {
												"$sort": { "created_time": -1 }
											}
										]);
		for storie in stories:
			storie_id = storie["users_storie"][0]["storie_id"]
			storie_user_id = storie["users_storie"][0]["user_id"]
			storieJson = UserDataModel.get_user_reduced_data_by_user_id(storie_user_id)
			storieJson["user_id"] = storieJson.pop("_id")
			storieJson["_id"] = storie_id
			storieJson["_rev"] = storie["_rev"]
			storieJson["created_time"] = str(storie["created_time"])
			storieJson["updated_time"] = str(storie["updated_time"])
			storieJson["title"] = storie["title"]
			storieJson["description"] = storie["description"]
			storieJson["location"] = storie["location"]
			storieJson["visibility"] = storie["visibility"]
			storieJson["multimedia"] = storie["multimedia"]
			storieJson["story_type"] = storie["story_type"]
			storieJson["comments"] = CommentModel.get_last_storie_comment(storie_id)
			storieJson["reactions"] = ReactionModel.get_storie_reactions(storie_id, user_id)
			data.append(storieJson)
		
		return data
		
	@staticmethod
	def get_stories_by_user_id(user_id):
		data = []
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		
		stories = db.stories.aggregate([
										   {
											  "$lookup":
												 {
													"from": "users_stories",
													"localField": "_id",
													"foreignField": "storie_id",
													"as": "users_storie"
												}
										   },
										   {
											  "$match": { "users_storie.user_id": user_id }
										   },
										   {
												"$sort": { "created_time": -1 }
											}
										]);
		
		for storie in stories:
			storie_id = storie["users_storie"][0]["storie_id"]
			storieJson = UserDataModel.get_user_reduced_data_by_user_id(user_id)
			storieJson["user_id"] = storieJson.pop("_id")
			storieJson["_id"] = storie_id
			storieJson["_rev"] = storie["_rev"]
			storieJson["created_time"] = str(storie["created_time"])
			storieJson["updated_time"] = str(storie["updated_time"])
			storieJson["title"] = storie["title"]
			storieJson["description"] = storie["description"]
			storieJson["location"] = storie["location"]
			storieJson["visibility"] = storie["visibility"]
			storieJson["multimedia"] = storie["multimedia"]
			storieJson["story_type"] = storie["story_type"]
			storieJson["comments"] = CommentModel.get_last_storie_comment(storie_id)
			storieJson["reactions"] = ReactionModel.get_storie_reactions(storie_id, user_id)
			data.append(storieJson)
		
		
		return data
		
	@staticmethod
	def delete_storie(storie_id, storie_user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		
		us_storie = db.users_stories.find_one({'storie_id': storie_id,'user_id': storie_user_id})

		if us_storie == None:
			raise NoStorieFoundException

		storie = db.stories.find_one({'_id': storie_id})
		db.stories.remove({'_id': storie_id})
		db.users_stories.remove({'storie_id': storie_id,'user_id': storie_user_id})
		storie['created_time'] = str(storie['created_time'])
		storie['updated_time'] = str(storie['updated_time'])
		return storie
	
	@staticmethod
	def get_new_storie(storie_id, rev, created_time, updated_time, title, desc, location, visibility, mult, story_type):
		return{ 
			"_id": storie_id,
			"_rev" : rev,
			"created_time" : created_time, 
			"updated_time" : updated_time, 
			"title" : title, 
			"description" : desc, 
			"location" : location, 
			"visibility" : visibility, 
			"multimedia" : mult, 
			"story_type" : story_type
		}
