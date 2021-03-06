import uuid

import pymongo

from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.date_controller import DateController
from controllers.db_controller import MongoController
from errors_exceptions.data_version_exception import DataVersionException
from errors_exceptions.no_storie_found_exception import NoStorieFoundException
from models.comment import CommentModel
from models.friend import FriendModel
from models.reaction import ReactionModel
from models.user_activity import UserActivityModel
from models.user_data import UserDataModel
from rules.rules import RulesMachine
from rules.storie_priority_data import StoriePriorityData


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
		HOURS_FAST_STORIES = 4
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
		expired_time = DateController.get_date_time_inc_by_hours(HOURS_FAST_STORIES) if (story_type == "fast") else ""
		user_id = body['user_id']

		storie = StorieModel.get_new_storie(storie_id, rev, user_id, created_time, updated_time, expired_time, title, desc, location, visibility, mult, story_type)
		db.stories.insert(storie)
		storie = StorieModel.format_storie_dates(storie)

		return storie

	@staticmethod
	def format_storie_dates(storie):
		storie['created_time'] = DateController.get_date_time_with_format(storie['created_time'])
		storie['updated_time'] = DateController.get_date_time_with_format(storie['updated_time'])
		storie['expired_time'] = DateController.get_date_time_with_format(storie['expired_time'])
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
		expired_time = act_storie["expired_time"]
		updated_time = DateController.get_date_time()
		title = body["title"]
		desc = body["description"] if ("description" in body) else ""
		location = body["location"]
		visibility = body["visibility"]
		mult = body["multimedia"]
		story_type = body["story_type"]
		user_id = body["user_id"]

		storie = StorieModel.get_new_storie(storie_id, rev, user_id, created_time, updated_time, expired_time, title, desc, location, visibility, mult, story_type)

		del storie['_id']
		res = db.stories.find_and_modify({'_id': storie_id},{'$set': storie})
		res = db.stories.find_one({'_id': storie_id})
		res = StorieModel.format_storie_dates(res)

		return res

	@staticmethod
	def get_stories(user_id, story_type = 'normal'):
		data = []
		sp_list = []
		stories_list = {}
		users_activity = {}
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		UserDataModel.exist_user(user_id)
		friends_id = FriendModel.get_friends_array_by_user_id(user_id)
		friends_id.append(user_id)

		opt1 = {"expired_time": ""}
		opt2 = {"expired_time": {"$gte": DateController.get_date_time()}}

		stories = db.stories.find({
			"$or" : [
				{
					"$and" : [
						{ "story_type": story_type},
						{ "$or" : [ opt1, opt2 ] },
						{ "user_id": {"$in": friends_id}}
					]
				},
				{
					"$and" : [
						{ "story_type": story_type},
						{ "$or" : [ opt1, opt2 ] },
						{ "user_id": {"$nin": friends_id} },
						{ "visibility": "public" }
					]
				}
			]}).sort("created_time",pymongo.DESCENDING)

		for storie in stories:
			storie_user_id = storie["user_id"]
			storie_id = storie["_id"]
			storie = StorieModel.format_storie_dates(storie)
			storie = StorieModel.get_storie_with_user_data(storie)
			storie["comments"] = CommentModel.get_last_storie_comment(storie_id)
			storie["reactions"] = ReactionModel.get_storie_reactions(storie_id, user_id)

			storie_data = StorieModel.get_storie_resume(storie)
			USER_ACT_PAST_DAYS = 10
			if (storie_user_id not in users_activity):
				users_activity[storie_user_id] = UserActivityModel.log_user_activity_resume(storie_user_id, USER_ACT_PAST_DAYS)

			storie_priority_data = StoriePriorityData(storie_id, storie_data["past"], storie_data["num_comments"], storie_data["num_reactions"], users_activity[storie_user_id]["num_friends"], users_activity[storie_user_id]["num_stories"])
			sp_list.append(storie_priority_data)
			RulesMachine.process_data(storie_priority_data)

			stories_list[storie_id] = storie

		sp_list.sort(key=lambda x: x.get_priority(), reverse=True)
		for sp in sp_list:
			data.append(stories_list[sp.get_storie_id()])

		return data

	@staticmethod
	def get_storie_resume(storie):
		storie_resume = {
				"storie_id": storie["_id"],
				"past": DateController.get_past_days(storie["created_time"]),
				"num_comments": CommentModel.count_storie_comments(storie["_id"]),
				"num_reactions": (storie["reactions"]["LIKE"]["count"] +
								storie["reactions"]["NOTLIKE"]["count"] +
								storie["reactions"]["GETBORED"]["count"] +
								storie["reactions"]["ENJOY"]["count"]
				)
		}

		return storie_resume

	@staticmethod
	def get_profile_stories_by_user_id(user_id):
		data = []
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)

		opt1 = {"expired_time": ""}
		opt2 = {"expired_time": {"$gte": DateController.get_date_time()}}

		stories = db.stories.find({

									"$and" : [
											{ "$or" : [ opt1, opt2 ] },
											{'user_id': user_id}
									]

								}).sort('created_time',pymongo.DESCENDING);

		for storie in stories:
			storie_id = storie["_id"]
			storie = StorieModel.format_storie_dates(storie)
			storie["comments"] = CommentModel.get_last_storie_comment(storie_id)
			storie["reactions"] = ReactionModel.get_storie_reactions(storie_id, user_id)
			storie_with_user_data = StorieModel.get_storie_with_user_data(storie)
			data.append(storie_with_user_data)

		return data

	@staticmethod
	def get_storie_with_user_data(storie):
		user_id = storie["user_id"]
		user_data = UserDataModel.get_user_reduced_data_by_user_id(user_id)
		storie_with_user_data = {**user_data, **storie}
		return storie_with_user_data

	@staticmethod
	def delete_storie(storie_id, storie_user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)

		storie = db.stories.find_one({'_id': storie_id,'user_id': storie_user_id})

		if storie == None:
			raise NoStorieFoundException

		CommentModel.remove_comment_by_storie_id(storie_id)
		ReactionModel.remove_reaction_by_storie_id(storie_id)
		db.stories.remove({'_id': storie_id})
		storie = StorieModel.format_storie_dates(storie)

		return storie

	@staticmethod
	def get_new_storie(storie_id, rev, user_id, created_time, updated_time, expired_time, title, desc, location, visibility, mult, story_type):
		return{
			"_id": storie_id,
			"_rev" : rev,
			"user_id" : user_id,
			"created_time" : created_time,
			"updated_time" : updated_time,
			"expired_time" : expired_time,
			"title" : title,
			"description" : desc,
			"location" : location,
			"visibility" : visibility,
			"multimedia" : mult,
			"story_type" : story_type
		}

	@staticmethod
	def count_stories(story_type = 'normal'):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		count = db.stories.find({"story_type": story_type}).count()
		return count

	@staticmethod
	def count_today_stories(story_type = 'normal'):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		date_from = DateController.today()
		date_to = DateController.tomorrow()
		count = db.stories.find({
			"$and" : [
				{ "story_type": story_type},
				{ "created_time" : {'$gte': date_from} },
				{ "created_time" : {'$lt': date_to} },
			]
		}).count()
		return count

	@staticmethod
	def get_storie(storie_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		storie = db.stories.find_one({"_id": storie_id})
		if storie is None:
			raise NoStorieFoundException
		return storie
