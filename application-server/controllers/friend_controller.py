import flask_restful
import json
from flask import request
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.friend import FriendModel
from errors_exceptions.no_data_found_exception import NoDataFoundException

class FriendController(flask_restful.Resource):
	
	def get(self, user_id):
		return self.get_friends_by_user_id(user_id)
		
	def get_friends_by_user_id(self, user_id):
		try:
			friends = FriendModel.get_friends_by_user_id(user_id)
			return self._create_get_friends_response(friends)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
	
	def create_friendship(self, friend):
		friend = FriendModel.create_friend(friend)
		return friend
		
	def _create_get_friends_response(self, friends):
		response = []
		for doc in friends:
			response.append(self._format_friend(doc))
		return response

	def _format_friend(self, friend):
        	return {
					'_id': friend['_id'],
					'user_id': friend['user_id'],
            		'last_name': friend['last_name'],
            		'name': friend['name'],
            		'date': friend['date'],
            		'picture': friend['picture']
        	}
