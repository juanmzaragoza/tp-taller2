import flask_restful
import json
from flask import request
from controllers.friend_controller import FriendController
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.friend_request import FriendRequestModel
from errors_exceptions.no_data_found_exception import NoDataFoundException

class FriendRequestDetailController(flask_restful.Resource):
	
	def get(self, request_id):
		try:
			 self._validate_request_id(request_id)
			 friend_request = FriendRequestModel.get_friend_request(request_id)
			 return self._get_friends_requests_response(friend_request)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
			
	def delete(self, request_id):
		try:
			 self._validate_request_id(request_id)
			 friend_request = FriendRequestModel.remove_friend_request(request_id)
			 return self._get_friends_requests_response(friend_request)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
			
	def _validate_request_id(self, requestId):
		 return FriendRequestModel.exists_request(requestId)
		 		 
	def _get_friends_requests_response(self, friends_requests):
		return friends_requests
