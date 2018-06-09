import flask_restful
import json
from flask import request
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.friend import FriendModel
from errors_exceptions.no_data_found_exception import NoDataFoundException

class FriendDetailController(flask_restful.Resource):
	def delete(self, friend_id):
		try:
			#body = json.loads(request.data.decode('utf-8'))
			friend_response = FriendModel.delete_friend(friend_id)
			return ResponseBuilder.build_response(friend_response, 200)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
