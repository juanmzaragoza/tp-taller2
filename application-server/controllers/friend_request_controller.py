import flask_restful
import json
from flask import request
from controllers.friend_controller import FriendController
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.friend_request import FriendRequestModel
from errors_exceptions.no_data_found_exception import NoDataFoundException

class FriendRequestController(flask_restful.Resource):

	def post(self):
		try:
			 request_id = self._get_request_id(request)
			 self._validate_request_id(request_id)
			 friends_requests = self._accept_friend_request(request_id)
			 return self._get_friends_requests_response(friends_requests)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
	
	def _accept_friend_request(self, request_id):
		friend_request = FriendRequestModel.remove_friend_request(request_id)
		friend_request.pop('_id')
		friend_res = FriendController()
		friend = friend_res.create_friendship(friend_request)
		return friend

	def _validate_request_id(self, requestId):
		 return FriendRequestModel.exists_request(requestId)
	
	def _get_friends_requests_response(self, friends_requests):
		return friends_requests
		
	def _get_request_id(self, request):
		 body = request.get_json()
		 return body['requestId']
