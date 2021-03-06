import flask_restful

from api_client.db_connection_error import DBConnectionError
from auth_service import login_required, validate_sender
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from errors_exceptions.no_user_data_found_exception import NoUserDataFoundException
from errors_exceptions.user_mismatch_exception import UserMismatchException
from models.friend_request import FriendRequestModel
from models.user_data import UserDataModel


class BeFriendDetailController(flask_restful.Resource):

	@login_required
	def get(self, user_id):
		try:
			self._validate_user_id(user_id)
			validate_sender(user_id)
			friends_requests = FriendRequestModel.get_friends_requests_rcv_by_user_id(user_id)
			return self._create_get_response(friends_requests)
		except NoUserDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except UserMismatchException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	def _create_get_response(self, friends_requests):
		return ResponseBuilder.get_build_response(friends_requests, 'requests', 200)

	# se usa? Si, lo llaman otros modulos
	def get_friends_requests_rcv_by_user_id(self, user_id):
		try:
			friends_requests = FriendRequestModel.get_friends_requests_rcv_by_user_id(user_id)
			return self._create_get_friends_requests_response(friends_requests)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	# se usa? Si, lo llaman otros modulos
	def get_friends_requests_sent_by_user_id(self, user_id):
		try:
			friends_requests = FriendRequestModel.get_friends_requests_sent_by_user_id(user_id)
			return self._create_get_friends_requests_response(friends_requests)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	def _validate_user_id(self, user_id):
		 return UserDataModel.exist_user(user_id)

	def _create_get_friends_requests_response(self, friends_requests):
		response = []
		for req in friends_requests:
			response.append(req)
		return response
