import uuid
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
		created_time = body["created_time"] 
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
		
		return res

	@staticmethod
	def get_stories(user_id):
		data = {}
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
												"$sort": { "created_time": 1 }
											}
										]);
		
		for storie in stories:
			storie_id = storie["users_storie"][0]["storie_id"]
			storie_user_id = storie["users_storie"][0]["user_id"]
			data[storie_id] = UserDataModel.get_user_reduced_data_by_user_id(storie_user_id)
			data[storie_id]["user_id"] = data[storie_id].pop("_id")
			data[storie_id]["_id"] = storie_id
			data[storie_id]["_rev"] = storie["_rev"]
			data[storie_id]["created_time"] = storie["created_time"]
			data[storie_id]["updated_time"] = storie["updated_time"]
			data[storie_id]["title"] = storie["title"]
			data[storie_id]["description"] = storie["description"]
			data[storie_id]["location"] = storie["location"]
			data[storie_id]["visibility"] = storie["visibility"]
			data[storie_id]["multimedia"] = storie["multimedia"]
			data[storie_id]["story_type"] = storie["story_type"]
			data[storie_id]["comments"] = CommentModel.get_storie_comments(storie_id)
			data[storie_id]["reactions"] = ReactionModel.get_storie_reactions(storie_id)
		
		return list(data.values())

	@staticmethod
	def get_stories_by_user_id(user_id):
		data = {}
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
												"$sort": { "created_time": 1 }
											}
										]);
		
		for storie in stories:
			storie_id = storie["users_storie"][0]["storie_id"]
			data[storie_id] = UserDataModel.get_user_reduced_data_by_user_id(user_id)
			data[storie_id]["user_id"] = data[storie_id].pop("_id")
			data[storie_id]["_id"] = storie_id
			data[storie_id]["_rev"] = storie["_rev"]
			data[storie_id]["created_time"] = storie["created_time"]
			data[storie_id]["updated_time"] = storie["updated_time"]
			data[storie_id]["title"] = storie["title"]
			data[storie_id]["description"] = storie["description"]
			data[storie_id]["location"] = storie["location"]
			data[storie_id]["visibility"] = storie["visibility"]
			data[storie_id]["multimedia"] = storie["multimedia"]
			data[storie_id]["story_type"] = storie["story_type"]
			data[storie_id]["comments"] = CommentModel.get_storie_comments(storie_id)
			data[storie_id]["reactions"] = ReactionModel.get_storie_reactions(storie_id)
		
		return list(data.values())
		
	@staticmethod
	def delete_storie(storie_id, storie_user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		
		us_storie = db.users_stories.find_one({'storie_id': storie_id,'user_id': storie_user_id})

		if us_storie == None:
			raise NoStorieFoundException

		storie = db.stories.find_one({'_id': storie_id})
		db.stories.remove({'_id': storie_id})
		db.users_stories.remove({'storie_id': storie_id,'user_id': storie_user_id})

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
