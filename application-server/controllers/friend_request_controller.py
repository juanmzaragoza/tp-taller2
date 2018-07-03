import flask_restful
from flask import request
from flask_restful import reqparse
from werkzeug.exceptions import BadRequest

from api_client.db_connection_error import DBConnectionError
from auth_service import login_required, validate_sender
from controllers.error_handler import ErrorHandler
from controllers.friend_controller import FriendController
from errors_exceptions.friendship_already_exists_exception import FriendshipAlreadyExistsException
from errors_exceptions.no_friend_request_found_exception import NoFriendRequestFoundException
from errors_exceptions.user_mismatch_exception import UserMismatchException
from models.friend_request import FriendRequestModel
from models.user_activity import UserActivityModel


class FriendRequestController(flask_restful.Resource):

	def __init__(self):
		self.parser = reqparse.RequestParser(bundle_errors=True)

	@login_required
	def post(self):
		try:
			self.parser.add_argument('request_id', required=True, help="Field request_id is mandatory")

			args = self.parser.parse_args()

			request_id = self._get_request_id(request)
			self._validate_request_id(request_id)
			self._validate_receiver(request_id)
			friend = self._accept_friend_request(request_id)
			print (friend)
			UserActivityModel.log_friend_activity(friend["user_id_rcv"], friend["user_id_sender"], "ADD")
			UserActivityModel.log_friend_activity(friend["user_id_sender"], friend["user_id_rcv"], "ADD")

			return self._get_friends_requests_response(friend)

		except BadRequest as ex:
			return ErrorHandler.create_error_response("Fields request_id are mandatory", 400)
		except NoFriendRequestFoundException as e:
			return ErrorHandler.create_error_response(str(e), 400)
		except FriendshipAlreadyExistsException as e:
			return ErrorHandler.create_error_response(str(e), 400)
		except UserMismatchException as e:
			return ErrorHandler.create_error_response(str(e), 409)
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
		return body['request_id']

	def _validate_receiver(self, request_id):
		friend_request = FriendRequestModel.get_friend_request(request_id)
		receiver_id = friend_request.get('user_id_rcv')
		validate_sender(receiver_id)
