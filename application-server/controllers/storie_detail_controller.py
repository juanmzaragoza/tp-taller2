import json
import flask_restful
from flask import request
from flask_restful import reqparse
from models.storie import StorieModel
from werkzeug.exceptions import BadRequest
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from api_client.db_connection_error import DBConnectionError
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.data_version_exception import DataVersionException
from errors_exceptions.no_storie_found_exception import NoStorieFoundException


class StorieDetailController(flask_restful.Resource):
	
	def __init__(self):
		self.parser = reqparse.RequestParser(bundle_errors=True)
		
	def get(self, id):
		try:
			 stories = StorieModel.get_stories(id)
			 return self._create_get_stories_response(stories)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
	
	def put(self, id):
		try:
			self.parser.add_argument('_id', required=True, help="Field id is mandatory")
			self.parser.add_argument('_rev', required=True, help="Field rev is mandatory")
			self.parser.add_argument('title', required=True, help="Field title is mandatory")
			self.parser.add_argument('location', required=True, help="Field location is mandatory")
			self.parser.add_argument('visibility', required=True, help="Field visibility is mandatory")
			self.parser.add_argument('multimedia', required=True, help="Field multimedia is mandatory")
			self.parser.add_argument('story_type', required=True, help="Field story_type is mandatory")
			self.parser.add_argument('user_id', required=True, help="Field user_id is mandatory")

			args = self.parser.parse_args()

			body = json.loads(request.data.decode('utf-8'))
			
			storie_response = StorieModel.update_storie(id, body)
			return ResponseBuilder.build_response(storie_response, 200)
			
		except BadRequest as ex:
			return ErrorHandler.create_error_response("Fields id, rev, title, location, user_id, visibility, multimedia and story_type are mandatory", 400)
		except NoStorieFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DataVersionException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
			
	def delete(self, id):
		try:
			body = json.loads(request.data.decode('utf-8'))
			storie_user_id = body['user_id']
			storie_response = StorieModel.delete_storie(id, storie_user_id)
			return ResponseBuilder.build_response(storie_response, 200)
		except NoStorieFoundException as e:
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
		for storie in stories:
			response.append(storie)
		return response
