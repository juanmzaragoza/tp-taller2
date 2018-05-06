import flask_restful
import json
from flask import request
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.friend import FriendModel
from errors_exceptions.no_data_found_exception import NoDataFoundException

class FriendController(flask_restful.Resource):

	def get_friends_by_user_id(self, user_id):
		try:
			friends = FriendModel.get_friends_by_user_id(user_id)
			return self.__create_get_friends_response(friends)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	def __create_get_friends_response(self, friends):
		response = []
		for doc in friends:
			response.append(self.__format_friend(doc))
		return response

	def __format_friend(self, friend):
        	return {
					'user_id': friend['user_id'],
            		'last_name': friend['last_name'],
            		'name': friend['name'],
            		'date': friend['date']
        	}
