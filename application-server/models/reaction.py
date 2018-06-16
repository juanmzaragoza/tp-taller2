import uuid
from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.date_controller import DateController
from errors_exceptions.data_version_exception import DataVersionException
from errors_exceptions.no_storie_found_exception import NoStorieFoundException
from errors_exceptions.no_reaction_found_exception import NoReactionFoundException

class ReactionModel:
	
	@staticmethod
	def remove_reaction(reaction_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		reaction = db.storie_reactions.find_one({"_id": reaction_id})
		
		if reaction == None:
			raise NoReactionFoundException
			
		db.storie_reactions.remove({"_id": reaction_id})

		return reaction
	
	@staticmethod
	def create_reaction(body):
		from models.storie import StorieModel
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		storie_id = body["storie_id"]

		if StorieModel.storie_exists(storie_id) == False:
			raise NoStorieFoundException
			
		reaction_id = str(uuid.uuid4().hex)
		user_id = body["user_id"]
		rev = ""
		reaction_date = DateController.get_date_time()
		reaction = body["reaction"]
		
		reaction = ReactionModel.get_new_reaction(reaction_id, storie_id, user_id, rev, reaction_date, reaction)
			
		db.storie_reactions.insert(reaction)

		return reaction
	
	@staticmethod
	def get_storie_reactions(storie_id):
		response = []
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		reactions = db.storie_reactions.find({'storie_id': storie_id})
		
		for reaction in reactions:
			response.append(reaction)
		print("storie_id")
		print(storie_id)
		return response

	@staticmethod
	def get_new_reaction(reaction_id, storie_id, user_id, rev, date, reaction):
		return {
			"_id": reaction_id,
			"storie_id": storie_id,
			"user_id": user_id,
			"_rev": rev,
			"date": date,
			"reaction": reaction
		}
