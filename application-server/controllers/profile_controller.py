import flask_restful
import json
from flask import request, jsonify
from controllers.response_builder import ResponseBuilder
from controllers.storie_controller import StorieController
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.profile import ProfileModel
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.data_version_exception import DataVersionException
from bson.json_util import dumps

class ProfileController(flask_restful.Resource):
	
	def get(self, user_id):
		try:
			profile_response = ProfileModel.get_profile_by_user_id(user_id)
			profile_id = str(profile_response.get('_id'))
			storie_controller = StorieController()
			stories_response = storie_controller.get_stories_by_profile_id(profile_id)
			return self.__create_get_response(profile_response, stories_response)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
	
	def put(self, user_id):
		try:
			body = json.loads(request.data.decode('utf-8'))
			profile_response = ProfileModel.update_profile_by_user_id(user_id, body)
			return self.__create_put_response(profile_response)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DataVersionException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
			
	def __create_get_response(self, profile, stories):
		profile_json = self.__get_profile_json(profile)
		profile_json['stories'] = stories
		return ResponseBuilder.get_build_response(profile_json, 'profile', 200)
	
	def __create_put_response(self, profile):
		profile_json = self.__get_profile_json(profile)
		return ResponseBuilder.build_response(profile_json, 200)
		
	def __get_profile_json(self, profile):
		profile_json = {	
			'_id' : str(profile.get('_id')),
			'_rev' : profile.get('_rev'),
			'last_name' : profile.get('last_name'),
			'name': profile.get('name'),
			'email': profile.get('email'),
			'profile_picture': profile.get('profile_picture')
		}
		
		return profile_json
