import uuid
from models.user_data import UserDataModel
from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.date_controller import DateController
from errors_exceptions.data_version_exception import DataVersionException
from errors_exceptions.no_comment_found_exception import NoCommentFoundException
from errors_exceptions.no_storie_found_exception import NoStorieFoundException

class CommentModel:
	
	@staticmethod
	def get_last_storie_comment(storie_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		response = []
		comment = db.storie_comments.find_one({'storie_id': storie_id},sort=[("date", -1)])
		
		if comment != None:
			user_comment = CommentModel.get_comment_with_user_data(comment)
			user_comment["date"] = DateController.get_date_time_with_format(user_comment["date"])
			response.append(user_comment)
		
		return response
		
	@staticmethod
	def remove_comment(comment_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		comment = db.storie_comments.find_one({"_id": comment_id})
		
		if comment == None:
			raise NoCommentFoundException
			
		db.storie_comments.remove({"_id": comment_id})
		comment["date"] = DateController.get_date_time_with_format(comment["date"])
		
		return comment
	
	@staticmethod
	def remove_comment_by_storie_id(storie_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		db.storie_comments.remove({"storie_id": storie_id})
		
	@staticmethod
	def update_comment(comment_id, body):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		
		comment = db.storie_comments.find_one({"_id": comment_id})
		
		if comment == None:
			raise NoCommentFoundException
		
		if comment["_rev"] != body.get("_rev"):
			raise DataVersionException

		comment_id = body['_id']
		storie_id = body["storie_id"]
		user_id = body["user_id"]
		UserDataModel.exist_user(user_id)
		rev = str(uuid.uuid4().hex)
		comment_date = DateController.get_date_time()
		message = body["message"]
		
		comment = CommentModel.get_new_comment(comment_id,storie_id,user_id,rev,comment_date,message)
		del comment['_id']
		
		comment = db.storie_comments.find_and_modify({'_id': comment_id},{'$set': comment})
		comment = db.storie_comments.find_one({'_id': comment_id})
		comment["date"] = DateController.get_date_time_with_format(comment["date"])
		return comment
	
	@staticmethod
	def create_comment(body):
		from models.storie import StorieModel
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		storie_id = body["storie_id"]

		if StorieModel.storie_exists(storie_id) == None:
			raise NoStorieFoundException
			
		comment_id = str(uuid.uuid4().hex)
		user_id = body["user_id"]
		
		UserDataModel.exist_user(user_id)
			
		rev = ""
		comment_date = DateController.get_date_time()
		message = body["message"]
		
		comment = CommentModel.get_new_comment(comment_id,storie_id,user_id,rev,comment_date,message)
			
		db.storie_comments.insert(comment)
		comment["date"] = DateController.get_date_time_with_format(comment["date"])
		return comment
	
	@staticmethod
	def get_storie_comments(storie_id):
		response = []
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		comments = db.storie_comments.find({'storie_id': storie_id})
		
		for com in comments:
			user_comment = CommentModel.get_comment_with_user_data(com)
			user_comment["date"] = DateController.get_date_time_with_format(user_comment["date"])
			response.append(user_comment)
		
		return response
		
	@staticmethod
	def get_comment_with_user_data(comment):
		user_id = comment["user_id"]
		user_data = UserDataModel.get_user_reduced_data_by_user_id(user_id)
		comment_with_user_data = {**user_data, **comment}
		return comment_with_user_data
		
	@staticmethod
	def get_new_comment(comment_id, storie_id, user_id, rev, date, message):
		return {
			"_id": comment_id,
			"storie_id": storie_id,
			"user_id": user_id,
			"_rev": rev,
			"date": date,
			"message": message
		}

	@staticmethod
	def count_comments():
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		count = db.storie_comments.find().count()
		return count

	@staticmethod
	def count_today_comments():
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		date_from = DateController.today()
		date_to = DateController.tomorrow()
		count = db.storie_comments.find({
			"$and" : [
				{ "date" : {'$gte': date_from} },
				{ "date" : {'$lt': date_to} }
			]
		}).count()
		return count
