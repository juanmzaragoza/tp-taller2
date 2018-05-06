import flask_restful
import json
from flask import request
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.storie import StorieModel
from errors_exceptions.no_data_found_exception import NoDataFoundException

class StorieController(flask_restful.Resource):

	def get_stories_by_user_id(self, user_id):
		try:
			stories = StorieModel.get_stories_by_user_id(user_id)
			return self.__create_get_stories_response(stories)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	def __create_get_stories_response(self, stories):
		response = []
		for doc in stories:
			response.append(self.__format_storie(doc))
		return response

	def __format_storie(self, storie):
        	return {
            		'_rev': storie['_rev'],
            		'created_time': storie['created_time'],
            		'updated_time': storie['updated_time'],
            		'title': storie['title'],
            		'description': storie['description'],
            		'location': storie['location'],
            		'visibility': storie['visibility'],
            		'multimedia': storie['multimedia'],
            		'story_type': storie['story_type']
        	}
