import json
import flask_restful
from flask import request
from models.friend import FriendModel
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from api_client.db_connection_error import DBConnectionError
from errors_exceptions.no_friend_found_exception import NoFriendFoundException

class FriendDetailController(flask_restful.Resource):
	def delete(self, friend_id):
		try:
			friend_response = FriendModel.delete_friend(friend_id)
			return ResponseBuilder.build_response(friend_response, 200)
		except NoFriendFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
