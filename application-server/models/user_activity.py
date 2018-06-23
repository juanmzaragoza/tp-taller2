from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.date_controller import DateController

class UserActivityModel():
	'''
	@staticmethod
	def update_user_activiy(user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		UserActivityModel._delete_user_activiy(user_id)
		activity = {
			"user_id": user_id,
			"date": DateController.now()
		}
		db.user_activities.insert(activity)
	'''
	@staticmethod
	def _delete_user_activiy(user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		db.user_activities.remove({'user_id': user_id})

	@staticmethod
	def count_today_activities():
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		date_from = DateController.today()
		date_to = DateController.tomorrow()
		count = db.user_activities.find({
			"$and" : [
				{ "date" : {'$gte': date_from} },
				{ "date" : {'$lt': date_to} }
			]
		}).count()
		return count
	
	@staticmethod
	def log_user_login_activity(user_id):
		login_activity = {
			"resource": "LOGIN",
			"date": DateController.get_date_time(),
			"user_id": user_id
		}
		
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		db.user_activities.insert(login_activity)
		
	@staticmethod
	def log_friend_activity(user_id, friend_id, op):
		friend_activity = {
			"resource": "FRIEND",
			"date": DateController.get_date_time(),
			"user_id": user_id,
			"friend_id": friend_id,
			"operation": op
		}
		
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		db.user_activities.insert(friend_activity)
	
	@staticmethod
	def log_comment_activity(user_id, storie_id, op):
		comment_activity = {
			"resource": "COMMENT",
			"date": DateController.get_date_time(),
			"user_id": user_id,
			"storie_id": storie_id,
			"operation": op
		}
		
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		db.user_activities.insert(comment_activity)
	
	@staticmethod
	def log_reaction_activity(user_id, storie_id, reaction, op):
		reaction_activity = {
			"resource": "REACTION",
			"date": DateController.get_date_time(),
			"user_id": user_id,
			"storie_id": storie_id,
			"reaction": reaction,
			"operation": op
		}
		
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		db.user_activities.insert(reaction_activity)
	
	@staticmethod
	def log_storie_activity(user_id, storie_id, op):
		storie_activity = {
			"resource": "STORIE",
			"date": DateController.get_date_time(),
			"user_id": user_id,
			"storie_id": storie_id,
			"operation": op
		}
		
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		db.user_activities.insert(storie_activity)

	@staticmethod
	def log_user_activity_resume(user_id, days):
		date_from = DateController.get_date_time_dec_by_days(days)
		date_to = DateController.get_date_time()
		user_resume = {
			"from": DateController.get_date_time_with_format(date_from),
			"to": DateController.get_date_time_with_format(date_to),
			"user_id": user_id,
			"num_stories": UserActivityModel.get_size_stories_by_user_id(user_id, date_from, date_to),
			"num_comments": UserActivityModel.get_size_comments_by_user_id(user_id, date_from, date_to),
			"num_reactions": UserActivityModel.get_size_reactions_by_user_id(user_id, date_from, date_to),
			"num_friends": UserActivityModel.get_size_friends_by_user_id(user_id, date_from, date_to)
		}
		
		return user_resume
	
	@staticmethod
	def get_size_stories_by_user_id(user_id, date_from, date_to):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		count = db.user_activities.find({
			"$and" : [
				{ "date" : {'$gte': date_from} },
				{ "date" : {'$lt': date_to} },
				{ "resource": "STORIE"},
				{ "user_id": user_id}
			]
		}).count()
		return count
	
	@staticmethod
	def get_size_comments_by_user_id(user_id, date_from, date_to):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		count = db.user_activities.find({
			"$and" : [
				{ "date" : {'$gte': date_from} },
				{ "date" : {'$lt': date_to} },
				{ "resource": "COMMENT"},
				{ "user_id": user_id}
			]
		}).count()
		return count
	
	@staticmethod
	def get_size_reactions_by_user_id(user_id, date_from, date_to):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		count = db.user_activities.find({
			"$and" : [
				{ "date" : {'$gte': date_from} },
				{ "date" : {'$lt': date_to} },
				{ "resource": "REACTION"},
				{ "user_id": user_id}
			]
		}).count()
		return count
	
	@staticmethod
	def get_size_friends_by_user_id(user_id, date_from, date_to):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		count = db.user_activities.find({
			"$and" : [
				{ "date" : {'$gte': date_from} },
				{ "date" : {'$lt': date_to} },
				{ "resource": "FRIEND"},
				{ "user_id": user_id}
			]
		}).count()
		return count
