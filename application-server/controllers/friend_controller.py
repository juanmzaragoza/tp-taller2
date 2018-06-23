import json
import flask_restful
from flask import request
from models.friend import FriendModel
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from api_client.db_connection_error import DBConnectionError
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.no_friend_found_exception import NoFriendFoundException
from auth_service import login_required
from models.user_activity import UserActivityModel

class FriendController(flask_restful.Resource):
	
	def get(self, id):
		return self.get_friends_by_user_id(id)
	
	def delete(self, id):
		try:
			friend = FriendModel.delete_friend(id)
			UserActivityModel.log_friend_activity(friend["user_id_rcv"], friend["user_id_sender"], "DELETE")
			UserActivityModel.log_friend_activity(friend["user_id_sender"], friend["user_id_rcv"], "DELETE")
			return ResponseBuilder.build_response(friend, 200)
		except NoFriendFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
	
	def get_friends_by_user_id(self, user_id):
		try:
			friends = FriendModel.get_friends_by_user_id(user_id)
			return self._create_get_friends_response(friends)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
	
	def create_friendship(self, friend):
		friend = FriendModel.create_friend(friend)
		return friend
		
	def _create_get_friends_response(self, friends):
		response = []
		for friend in friends:
			response.append(friend)
		return response
