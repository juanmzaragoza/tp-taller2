import json
import flask_restful
from bson.json_util import dumps
from flask import request, jsonify
from flask_restful import reqparse
from models.user_data import UserDataModel
from werkzeug.exceptions import BadRequest
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from controllers.friend_controller import FriendController
from api_client.db_connection_error import DBConnectionError
from controllers.storie_detail_controller import StorieDetailController
from errors_exceptions.data_version_exception import DataVersionException
from errors_exceptions.no_user_data_found_exception import NoUserDataFoundException
from controllers.be_friend_detail_controller import BeFriendDetailController
from auth_service import login_required

class ProfileController(flask_restful.Resource):

	def __init__(self):
		self.parser = reqparse.RequestParser(bundle_errors=True)

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
		except NoUserDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	def put(self, user_id):
		try:

			self.parser.add_argument('_id', required=True, help="Field id is mandatory")
			self.parser.add_argument('_rev', required=True, help="Field rev is mandatory")
			#self.parser.add_argument('user_name', required=True, help="Field user_name is mandatory")
			self.parser.add_argument('last_name', required=True, help="Field last_name is mandatory")
			self.parser.add_argument('name', required=True, help="Field name is mandatory")
			self.parser.add_argument('email', required=True, help="Field email is mandatory")
			self.parser.add_argument('birthday', required=True, help="Field birthday is mandatory")
			self.parser.add_argument('gender', required=True, help="Field gender is mandatory")

			args = self.parser.parse_args()

			body = json.loads(request.data.decode('utf-8'))
			user_data_response = UserDataModel.update_user_data_by_user_id(user_id, body)
			return self.__create_put_response(user_data_response)

		except BadRequest as ex:
			return ErrorHandler.create_error_response("Fields id, rev, last_name, name, birthday, gender, and email are mandatory", 400)
		except NoUserDataFoundException as e:
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
		return ResponseBuilder.build_response(user_data, 200)

	def __get_profile_json(self, user_data):
		profile_json = {
			'_id' : str(user_data.get('_id')),
			'_rev' : user_data.get('_rev'),
			'last_name' : user_data.get('last_name'),
			'name': user_data.get('name'),
			'email': user_data.get('email'),
			'birthday': user_data.get('birthday'),
			'gender': user_data.get('gender'),
			'picture': user_data.get('picture')
		}

		return profile_json
