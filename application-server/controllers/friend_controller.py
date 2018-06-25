import json
import flask_restful
from flask import request
from models.friend import FriendModel
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from api_client.db_connection_error import DBConnectionError
from errors_exceptions.no_friend_found_exception import NoFriendFoundException
from errors_exceptions.user_mismatch_exception import UserMismatchException
from auth_service import login_required, get_user_id
from models.user_activity import UserActivityModel

class FriendController(flask_restful.Resource):
	
	@login_required
	def get(self, id):
		return self.get_friends_by_user_id(id)
	
	@login_required
	def delete(self, id):
		try:
			self._validate_friendship(id)
			friend = FriendModel.delete_friend(id)
			UserActivityModel.log_friend_activity(friend["user_id_rcv"], friend["user_id_sender"], "DELETE")
			UserActivityModel.log_friend_activity(friend["user_id_sender"], friend["user_id_rcv"], "DELETE")
			return ResponseBuilder.build_response(friend, 200)
		except NoFriendFoundException as e:
			return ErrorHandler.create_error_response(str(e), 400)
		except UserMismatchException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
	
	def get_friends_by_user_id(self, user_id):
		try:
			friends = FriendModel.get_friends_by_user_id(user_id)
			return self._create_get_friends_response(friends)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
	
	# lo usa FriendRequestController
	def create_friendship(self, friend):
		friend = FriendModel.create_friend(friend)
		return friend
		
	def _create_get_friends_response(self, friends):
		response = []
		for friend in friends:
			response.append(friend)
		return response

	def _validate_friendship(self, friendship_id):
		friendship = FriendModel.get_friend(friendship_id)
		sender_user_id = int(friendship.get('user_id_sender'))
		receiver_user_id = int(friendship.get('user_id_rcv'))
		user_id = int(get_user_id())
		if (user_id not in [sender_user_id, receiver_user_id]):
			raise UserMismatchException()
