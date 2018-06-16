import uuid
from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.date_controller import DateController
from errors_exceptions.data_version_exception import DataVersionException
from errors_exceptions.no_comment_found_exception import NoCommentFoundException
from errors_exceptions.no_storie_found_exception import NoStorieFoundException

class CommentModel:
	
	@staticmethod
	def remove_comment(comment_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		comment = db.storie_comments.find_one({"_id": comment_id})
		
		if comment == None:
			raise NoCommentFoundException
			
		db.storie_comments.remove({"_id": comment_id})

		return comment
	
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
		rev = str(uuid.uuid4().hex)
		comment_date = DateController.get_date_time()
		message = body["message"]
		
		comment = CommentModel.get_new_comment(comment_id,storie_id,user_id,rev,comment_date,message)
		del comment['_id']
		
		comment = db.storie_comments.find_and_modify({'_id': comment_id},{'$set': comment})
		#comment = db.storie_comments.find_one({'_id': comment_id})

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
		rev = ""
		comment_date = DateController.get_date_time()
		message = body["message"]
		
		comment = CommentModel.get_new_comment(comment_id,storie_id,user_id,rev,comment_date,message)
			
		db.storie_comments.insert(comment)

		return comment
	
	@staticmethod
	def get_storie_comments(storie_id):
		response = []
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		comments = db.storie_comments.find({'storie_id': storie_id})
		
		for com in comments:
			response.append(com)
		
		return response
	
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
