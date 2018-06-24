import json
import flask_restful
from flask import request
from flask_restful import reqparse
from werkzeug.exceptions import BadRequest
from models.user_data import UserDataModel
from controllers.error_handler import ErrorHandler
from models.friend_request import FriendRequestModel
from controllers.friend_controller import FriendController
from controllers.response_builder import ResponseBuilder
from api_client.db_connection_error import DBConnectionError
from errors_exceptions.no_user_data_found_exception import NoUserDataFoundException
from errors_exceptions.data_already_exists_exception import DataAlreadyExistsException
from errors_exceptions.friendship_already_exists_exception import FriendshipAlreadyExistsException
from errors_exceptions.friend_request_already_exists_exception import FriendRequestAlreadyExistsException
from errors_exceptions.user_mismatch_exception import UserMismatchException
from auth_service import login_required, validate_sender

class BeFriendController(flask_restful.Resource):
	
	def __init__(self):
		self.parser = reqparse.RequestParser(bundle_errors=True)
		
	@login_required
	def post(self):
		try:
			self.parser.add_argument('user_id', required=True, help="Field user_id is mandatory")
			self.parser.add_argument('rcv_user_id', required=True, help="Field rcv_user_id is mandatory")

			args = self.parser.parse_args()
			user_sender_id, user_rcv_id, msg, picture = self._get_friend_request_data(args)
			self._validate_user_id(user_sender_id)
			validate_sender(user_sender_id)
			self._validate_user_id(user_rcv_id)
			
			be_friend_request = self._create_be_friend_request(user_sender_id, user_rcv_id, msg, picture)
			return self._get_be_friend_request_response(be_friend_request)
		
		except BadRequest as ex:
			return ErrorHandler.create_error_response("Fields user_id and rcv_user_id are mandatory", 400)
		except DataAlreadyExistsException as e:
			return ErrorHandler.create_error_response(str(e), 400)
		except FriendRequestAlreadyExistsException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except FriendshipAlreadyExistsException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except NoUserDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except UserMismatchException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
					
	def _get_friend_request_data(self, args):
		user_id = args.get('user_id')
		rcv_user_id = args.get('rcv_user_id')
		message = args.get('message', None)
		picture = args.get('picture', None)
		return user_id, rcv_user_id, message, picture
		
	def _create_be_friend_request(self, user_sender_id, user_rcv_id, msg, picture):
		friend_request = FriendRequestModel.create_friend_request(user_sender_id, user_rcv_id, msg, picture)
		return friend_request

	def _validate_user_id(self, user_id):
		 return UserDataModel.exist_user(user_id)
	
	def _get_be_friend_request_response(self, be_friend_request):
		return be_friend_request