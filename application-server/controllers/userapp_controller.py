import json
import flask_restful
from bson.json_util import dumps
from flask import request, jsonify
from models.user_data import UserDataModel
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from api_client.db_connection_error import DBConnectionError

class UserAppController(flask_restful.Resource):
	
	def get(self, user_id):
		try:
			user_data_response = UserDataModel.get_all_users_except(user_id)
			return self._create_get_response(user_data_response)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
			
	def _create_get_response(self, user_data):
		return ResponseBuilder.get_build_response(user_data, 'users', 200)
