import flask_restful
import json
from flask import request, jsonify
from controllers.response_builder import ResponseBuilder
from controllers.storie_detail_controller import StorieDetailController
from controllers.friend_controller import FriendController
from controllers.be_friend_detail_controller import BeFriendDetailController
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.user_data import UserDataModel
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.data_version_exception import DataVersionException
from bson.json_util import dumps

class ProfileController(flask_restful.Resource):
	
	def get(self, user_id):
		try:
			user_data_response = UserDataModel.get_user_data_by_user_id(user_id)
			storie_detail_controller = StorieDetailController()
			stories_response = storie_detail_controller.get_stories_by_user_id(user_id)
			friend_controller = FriendController()
			friends_response = friend_controller.get_friends_by_user_id(user_id)
			be_friend_detail_controller = BeFriendDetailController()
			friends_requests_rcv = be_friend_detail_controller.get_friends_requests_rcv_by_user_id(user_id)
			friends_requests_sent = be_friend_detail_controller.get_friends_requests_sent_by_user_id(user_id)
			return self._create_get_response(user_data_response, stories_response, friends_response, friends_requests_rcv, friends_requests_sent)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
	
	def put(self, user_id):
		try:
			body = json.loads(request.data.decode('utf-8'))
			user_data_response = UserDataModel.update_user_data_by_user_id(user_id, body)
			return self.__create_put_response(user_data_response)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DataVersionException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
			
	def _create_get_response(self, user_data, stories, friends, friends_requests_rcv, friends_requests_sent):
		profile_json = self.__get_profile_json(user_data)
		profile_json['stories'] = stories
		profile_json['friends'] = friends
		profile_json['requests'] = {
										"sent": friends_requests_sent,
										"rcv": friends_requests_rcv
									}
		return ResponseBuilder.get_build_response(profile_json, 'profile', 200)
	
	def __create_put_response(self, user_data):
		profile_json = self.__get_profile_json(user_data)
		return ResponseBuilder.build_response(profile_json, 200)
		
	def __get_profile_json(self, user_data):
		profile_json = {	
			'_id' : str(user_data.get('_id')),
			'_rev' : user_data.get('_rev'),
			'last_name' : user_data.get('last_name'),
			'name': user_data.get('name'),
			'email': user_data.get('email'),
			'picture': user_data.get('picture')
		}
		
		return profile_json
