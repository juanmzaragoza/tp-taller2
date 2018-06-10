import flask_restful
import json
from flask import request
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.storie import StorieModel
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.data_version_exception import DataVersionException

class StorieDetailController(flask_restful.Resource):
	
	def get(self, id):
		try:
			 stories = StorieModel.get_stories(id)
			 return self._create_get_stories_response(stories)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
	
	def put(self, id):
		try:
			body = json.loads(request.data.decode('utf-8'))
			storie_response = StorieModel.update_storie(id, body)
			return ResponseBuilder.build_response(storie_response, 200)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DataVersionException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
			
	def delete(self, id):
		try:
			body = json.loads(request.data.decode('utf-8'))
			storie_response = StorieModel.delete_storie(id, body)
			return ResponseBuilder.build_response(storie_response, 200)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
						
	def get_stories_by_user_id(self, user_id):
		try:
			stories = StorieModel.get_stories_by_user_id(user_id)
			return self._create_get_stories_response(stories)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	def _create_get_stories_response(self, stories):
		response = []
		for doc in stories:
			response.append(self._format_storie(doc))
		return response

	def _format_storie(self, storie):
        	return {
            		'_id': storie['_id'],
            		'_rev': storie['_rev'],
            		'user_id': storie['user_id'],
            		'created_time': storie['created_time'],
            		'updated_time': storie['updated_time'],
            		'title': storie['title'],
            		'description': storie['description'],
            		'location': storie['location'],
            		'visibility': storie['visibility'],
            		'multimedia': storie['multimedia'],
            		'story_type': storie['story_type'],
            		'user_name': storie['user_name'],
            		'user_last_name': storie['user_last_name'],
            		'user_email': storie['user_email'],
            		'user_picture': storie['user_picture'],
            		'comments': storie['comments']
        	}
