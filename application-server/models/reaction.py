from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.data_version_exception import DataVersionException
from bson.objectid import ObjectId
import bson
import uuid
import time

class ReactionModel:
	
	@staticmethod
	def remove_reaction(reaction_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		reaction = db.storie_reactions.find_one({'_id': reaction_id})
		
		if reaction == None:
			raise NoDataFoundException
			
		db.storie_reactions.remove({'_id': reaction_id})

		return reaction
	
	@staticmethod
	def create_reaction(body):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		reaction_date = time.strftime('%d/%m/%Y %H:%M:%S', time.localtime())
		reaction_id = str(uuid.uuid4().hex)
		reactionJson = {
					"_id": reaction_id,
					"storie_id": body["storie_id"],
					"user_id": body["user_id"],
					"_rev": "",
					"date": reaction_date,
					"reaction": body["reaction"]
				}
			
		db.storie_reactions.insert(reactionJson)
		reaction = db.storie_reactions.find_one({'_id': reaction_id})

		return reaction
