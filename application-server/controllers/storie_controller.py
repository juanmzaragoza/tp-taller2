import flask_restful
import json
from flask import request
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.storie import StorieModel
from errors_exceptions.no_data_found_exception import NoDataFoundException

class StorieController(flask_restful.Resource):
	
	def post(self):
		try:
			 storie = self._create_user_storie_request(request)
			 return self._format_storie(storie)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	def _get_user_id(self, request):
		body = request.get_json()
		return body['userId']
	
	def _create_user_storie_request(self, request):
		body = request.get_json()
		storie = StorieModel.create_user_storie(body)
		return storie
		
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
            		'story_type': storie['story_type']
        	}
