import uuid
from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.date_controller import DateController
from errors_exceptions.data_version_exception import DataVersionException
from errors_exceptions.no_storie_found_exception import NoStorieFoundException
from errors_exceptions.no_reaction_found_exception import NoReactionFoundException
from errors_exceptions.storie_reaction_already_exists_exception import StorieReactionAlreadyFoundException

class ReactionModel:
	
	@staticmethod
	def remove_reaction(reaction_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		reaction = db.storie_reactions.find_one({"_id": reaction_id})
		
		if reaction == None:
			raise NoReactionFoundException
			
		db.storie_reactions.remove({"_id": reaction_id})
		reaction["date"] = DateController.get_date_time_with_format(reaction["date"])
		return reaction
	
	@staticmethod
	def create_reaction(body):
		from models.storie import StorieModel
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		storie_id = body["storie_id"]

		if StorieModel.storie_exists(storie_id) == False:
			raise NoStorieFoundException
		
		user_id = body["user_id"]
		
		if ReactionModel.reaction_exists(storie_id, user_id) == True:
			raise StorieReactionAlreadyFoundException
			
		reaction_id = str(uuid.uuid4().hex)
		rev = ""
		reaction_date = DateController.get_date_time()
		reaction = body["reaction"]
		
		reaction = ReactionModel.get_new_reaction(reaction_id, storie_id, user_id, rev, reaction_date, reaction)
			
		db.storie_reactions.insert(reaction)
		reaction["date"] = DateController.get_date_time_with_format(reaction["date"])
		
		return reaction
	
	@staticmethod
	def reaction_exists(storie_id, user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		react = db.storie_reactions.find_one({'storie_id': storie_id, 'user_id': user_id})
		
		if react == None:
			return False
		
		return True
		
	@staticmethod
	def get_storie_reactions(storie_id, user_id):
		response = {}
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		like = ReactionModel.get_storie_resume_reactions(storie_id, user_id, 'LIKE')
		notlike = ReactionModel.get_storie_resume_reactions(storie_id, user_id, 'NOTLIKE')
		enjoy = ReactionModel.get_storie_resume_reactions(storie_id, user_id, 'ENJOY')
		bored = ReactionModel.get_storie_resume_reactions(storie_id, user_id, 'GETBORED')
		
		response["LIKE"] = like
		response["NOTLIKE"] = notlike
		response["ENJOY"] = enjoy
		response["GETBORED"] = bored
		#reactions = db.storie_reactions.find({'storie_id': storie_id})
		
		#for reaction in reactions:
			#reaction["date"] = str(reaction["date"])
			#response.append(reaction)

		return response
	
	@staticmethod
	def get_storie_resume_reactions(storie_id, user_id, reaction):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		counter = db.storie_reactions.find({'storie_id': storie_id, 'reaction': reaction}).count()
		react = db.storie_reactions.find_one({'storie_id': storie_id, 'user_id': user_id, 'reaction': reaction},{"date":1, "_id":0})
		
		if (react != None):
			react["date"] = DateController.get_date_time_with_format(react["date"]) 

		return {
			"count": counter,
			"react": react
		}
		
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
