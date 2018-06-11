from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from errors_exceptions.no_data_found_exception import NoDataFoundException
from models.user_data import UserDataModel
from errors_exceptions.data_version_exception import DataVersionException
import bson
import uuid
import time

class StorieModel:
	@staticmethod
	def create_user_storie(body):
		db = MongoController.get_mongodb_instance(MONGODB_USER,
                	MONGODB_PASSWD)
		storie_date = time.strftime('%d/%m/%Y %H:%M:%S', time.localtime())
		storie_id = str(uuid.uuid4().hex)
		storie = {
			'_id': storie_id,
			'created_time': storie_date,
			'updated_time': "",
			'_rev': body['_rev'],
			'title': body['title'],
			'description': body['description'],
			'location': body['location'],
			'visibility': body['visibility'],
			'multimedia': body['multimedia'],
			'story_type': body['storyType']}
		#storie_id = db.stories.insert(storie)
		db.stories.insert(storie)
		db.users_stories.insert({'user_id': body['userId'],'storie_id': storie_id})
		response = db.stories.find_one({'_id': storie_id})

		response['user_id'] = body['userId']
		return response

	@staticmethod
	def update_storie(storie_id, body):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)

		storie = db.stories.find_one({'_id':  storie_id})

		if storie == None:
			raise NoDataFoundException

		if storie['_rev'] != body.get('_rev'):
			raise DataVersionException

		storie = db.users_stories.find_one({'storie_id': storie_id})

		if storie['user_id'] != body.get('user_id'):
			raise NoDataFoundException

		body['_rev'] = uuid.uuid4().hex
		del body['_id']
		storie = db.stories.find_and_modify({'_id': storie_id},{'$set': body})
		storie = db.stories.find_one({'_id': storie_id})
		storie['_id'] = str(storie['_id'])
		return storie

	@staticmethod
	def get_stories(user_id):
		stories_id = []
		response = {}
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		stories_regs = db.users_stories.find({'user_id': {'$ne': user_id}})
		for doc in stories_regs:
			storie_id = str(doc['storie_id'])
			user_data_response = UserDataModel.get_user_data_by_user_id(doc['user_id'])
			response[storie_id] = {
						'user_id': doc['user_id'],
						'user_last_name': user_data_response['last_name'],
						'user_name': user_data_response['name'],
						'user_email': user_data_response['email'],
						'user_picture': user_data_response['picture']
						}
			stories_id.append(storie_id)

		stories = db.stories.find({'_id': {'$in': stories_id}})
		for doc in stories:
			storie_id = str(doc['_id'])
			response[storie_id].update({'_id': storie_id})
			response[storie_id].update({'_rev': doc['_rev']})
			response[storie_id].update({'created_time': doc['created_time']})
			response[storie_id].update({'updated_time': doc['updated_time']})
			response[storie_id].update({'title': doc['title']})
			response[storie_id].update({'description': doc['description']})
			response[storie_id].update({'location': doc['location']})
			response[storie_id].update({'visibility': doc['visibility']})
			response[storie_id].update({'multimedia': doc['multimedia']})
			response[storie_id].update({'story_type': doc['story_type']})
			response[storie_id].update({'comments': []})
			response[storie_id].update({'reactions': []})
			comments = db.storie_comments.find({'storie_id': storie_id})
			for com in comments:
				response[storie_id]["comments"].append(com)
			reactions = db.storie_reactions.find({'storie_id': storie_id})
			for react in reactions:
				response[storie_id]["reactions"].append(react)
				
		return list(response.values())

	@staticmethod
	def get_stories_by_user_id(user_id):
		stories_id = []
		response = {}
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		stories_regs = db.users_stories.find({'user_id': user_id})
		for doc in stories_regs:
			storie_id = str(doc['storie_id'])
			user_data_response = UserDataModel.get_user_data_by_user_id(doc['user_id'])
			response[storie_id] = {
						'user_id': doc['user_id'],
						'user_last_name': user_data_response['last_name'],
						'user_name': user_data_response['name'],
						'user_email': user_data_response['email'],
						'user_picture': user_data_response['picture']}
			stories_id.append(doc['storie_id'])

		stories = db.stories.find({'_id': {'$in': stories_id}})
		for doc in stories:
			storie_id = str(doc['_id'])
			response[storie_id].update({'_id': storie_id})
			response[storie_id].update({'_rev': doc['_rev']})
			response[storie_id].update({'created_time': doc['created_time']})
			response[storie_id].update({'updated_time': doc['updated_time']})
			response[storie_id].update({'title': doc['title']})
			response[storie_id].update({'description': doc['description']})
			response[storie_id].update({'location': doc['location']})
			response[storie_id].update({'visibility': doc['visibility']})
			response[storie_id].update({'multimedia': doc['multimedia']})
			response[storie_id].update({'story_type': doc['story_type']})
			response[storie_id].update({'comments': []})
			response[storie_id].update({'reactions': []})
			comments = db.storie_comments.find({'storie_id': storie_id})
			for com in comments:
				response[storie_id]["comments"].append(com)
			reactions = db.storie_reactions.find({'storie_id': storie_id})
			for react in reactions:
				response[storie_id]["reactions"].append(react)
					
		return list(response.values())

	@staticmethod
	def delete_storie(storie_id, body):
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		
		storie = db.users_stories.find_one({'storie_id': storie_id,'user_id': body['user_id']})

		if storie == None:
			raise NoDataFoundException

		response = db.stories.find_one({'_id': storie_id})
		db.stories.remove({'_id': storie_id})
		db.users_stories.remove({'storie_id': storie_id,'user_id': body['user_id']})

		response['_id'] = str(response['_id'])
		return response
