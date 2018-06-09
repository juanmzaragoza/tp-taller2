import flask_restful
import json
from flask import request, jsonify
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.user_data import UserDataModel
from errors_exceptions.no_data_found_exception import NoDataFoundException
from bson.json_util import dumps

class UserAppController(flask_restful.Resource):
	
	def get(self, user_id):
		try:
			user_data_response = UserDataModel.get_all_users_except(user_id)
			return self._create_get_response(user_data_response)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
			
	def _create_get_response(self, user_data):
		return ResponseBuilder.get_build_response(user_data, 'users', 200)
