from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from errors_exceptions.no_data_found_exception import NoDataFoundException
from models.user_data import UserDataModel
from errors_exceptions.data_version_exception import DataVersionException
from bson.objectid import ObjectId
import bson
import uuid
import time

class CommentModel:
	
	@staticmethod
	def remove_comment(comment_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		comment = db.storie_comments.find_one({'_id': comment_id})
		
		if comment == None:
			raise NoDataFoundException
			
		db.storie_comments.remove({'_id': comment_id})

		return comment
	
	@staticmethod
	def update_comment(comment_id, body):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		
		comment = db.storie_comments.find_one({'_id': comment_id})
		
		if comment == None:
			raise NoDataFoundException
		
		if comment['_rev'] != body.get('_rev'):
			raise DataVersionException

		body['_rev'] = uuid.uuid4().hex
		del body['_id']
		
		comment = db.storie_comments.find_and_modify({'_id': comment_id},{'$set': body})
		comment = db.storie_comments.find_one({'_id': comment_id})

		return comment
	
	@staticmethod
	def create_comment(body):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		comment_date = time.strftime('%d/%m/%Y %H:%M:%S', time.localtime())
		comment_id = str(uuid.uuid4().hex)
		commentJson = {
					"_id": comment_id,
					"storie_id": body["storie_id"],
					"user_id": body["user_id"],
					"_rev": "",
					"date": comment_date,
					"message": body["message"]
				}
			
		db.storie_comments.insert(commentJson)
		comment = db.storie_comments.find_one({'_id': comment_id})

		return comment
