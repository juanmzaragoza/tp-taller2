import flask_restful
import json
from flask import request
from controllers.friend_controller import FriendController
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.user_data import UserDataModel
from models.friend_request import FriendRequestModel
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.data_already_exists_exception import DataAlreadyExistsException

class BeFriendController(flask_restful.Resource):

	def post(self):
		try:
			 user_sender_id, user_rcv_id, msg, picture = self._get_friend_request_data(request)
			 self._validate_user_id(user_sender_id)
			 self._validate_user_id(user_rcv_id)
			 be_friend_request = self._create_be_friend_request(user_sender_id, user_rcv_id, msg, picture)
			 return self._get_be_friend_request_response(be_friend_request)
		except DataAlreadyExistsException as e:
			return ErrorHandler.create_error_response(str(e), 400)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	def _get_friend_request_data(self, request):
		body = request.get_json()
		return body['user_id'], body['rcvUserId'], body['message'], body['picture']

	def _create_be_friend_request(self, user_sender_id, user_rcv_id, msg, picture):
		friend_request = FriendRequestModel.create_friend_request(user_sender_id, user_rcv_id, msg, picture)
		return friend_request

	def _validate_user_id(self, user_id):
		 return UserDataModel.exist_user(user_id)

	def _get_be_friend_request_response(self, be_friend_request):
		return be_friend_request
