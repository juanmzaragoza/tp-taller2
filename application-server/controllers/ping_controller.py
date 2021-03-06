import flask_restful

from api_client.db_connection_error import DBConnectionError
from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder


class PingController(flask_restful.Resource):

	def get(self):
		try:
			db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
			response = self.__ping_successful_response()
			code = 200
			return ResponseBuilder.get_build_response(response, "server", code)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	def __ping_successful_response(self):
		return {
			"status": "active"
		}
