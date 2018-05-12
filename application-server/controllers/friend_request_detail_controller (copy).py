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
			
	def get_friends_requests_rcv_by_user_id(self, user_id):
		try:
			friends_requests = FriendRequestModel.get_friends_requests_rcv_by_user_id(user_id)
			return self._create_get_friends_requests_response(friends_requests)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
	
	def get_friends_requests_sent_by_user_id(self, user_id):
		try:
			friends_requests = FriendRequestModel.get_friends_requests_sent_by_user_id(user_id)
			return self._create_get_friends_requests_response(friends_requests)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
			
	def _validate_request_id(self, requestId):
		 return FriendRequestModel.exists_request(requestId)
		 		 
	def _get_friends_requests_response(self, friends_requests):
		return friends_requests
	
	def _create_get_friends_requests_response(self, friends_requests):
		response = []
		for doc in friends_requests:
			response.append(self._format_friend_request(doc))
		return response

	def _format_friend_request(self, friend_request):
        	return {
					'_id': friend_request['_id'],
					'user_id': friend_request['user_id'],
            		'last_name': friend_request['last_name'],
            		'name': friend_request['name'],
            		'date': friend_request['date']
        	}
