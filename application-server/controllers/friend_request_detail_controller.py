import json
import flask_restful
from flask import request
from controllers.error_handler import ErrorHandler
from models.friend_request import FriendRequestModel
from controllers.response_builder import ResponseBuilder
from controllers.friend_controller import FriendController
from api_client.db_connection_error import DBConnectionError
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.no_friend_request_found_exception import NoFriendRequestFoundException
from errors_exceptions.user_mismatch_exception import UserMismatchException
from auth_service import login_required, get_user_id

class FriendRequestDetailController(flask_restful.Resource):
	
	@login_required
	def get(self, request_id):
		try:
			 self._validate_request_id(request_id)
			 friend_request = FriendRequestModel.get_friend_request(request_id)
			 self._validate_request_participant(friend_request)
			 return self._get_friends_requests_response(friend_request)
		except NoFriendRequestFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except UserMismatchException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
			
	@login_required
	def delete(self, request_id):
		try:
			self._validate_request_id(request_id)

			friend_request = FriendRequestModel.get_friend_request(request_id)
			self._validate_request_participant(friend_request)

			friend_request = FriendRequestModel.remove_friend_request(request_id)
			return self._get_friends_requests_response(friend_request)
		except NoFriendRequestFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except UserMismatchException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
			
	def _validate_request_id(self, requestId):
		 return FriendRequestModel.exists_request(requestId)
		 		 
	def _get_friends_requests_response(self, friends_requests):
		return friends_requests

	def _validate_request_participant(self, friend_request):
		sender_user_id = int(friend_request.get('user_id_sender'))
		receiver_user_id = int(friend_request.get('user_id_rcv'))
		user_id = int(get_user_id())
		if (user_id not in [sender_user_id, receiver_user_id]):
			raise UserMismatchException()
