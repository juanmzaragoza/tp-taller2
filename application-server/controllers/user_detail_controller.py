from flask_restful import Resource
from werkzeug.exceptions import BadRequest

from api_client.shared_api_client import SharedApiClient
from api_client.request_exception import RequestException


from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler

class UserDetailController(Resource):
	def __init__(self):
		self.shared_api_client = SharedApiClient()

	def get(self, user_id):
		if not user_id:
			return ErrorHandler.create_error_response("Field id is mandatory", 400)

		try:
			response = self.shared_api_client.getUserById(user_id)
			if not response:
				return ErrorHandler.create_error_response("User doesn't exists", 404)
			
			return ResponseBuilder.build_response(response, 200)

		except RequestException as e:
			return ErrorHandler.create_error_response(str(e), 500)

		except BadRequest as ex:
			return ErrorHandler.create_error_response(str(ex), 400)