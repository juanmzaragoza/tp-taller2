import flask_restful
from flask_restful import reqparse
from werkzeug.exceptions import BadRequest

from api_client.request_exception import RequestException
from api_client.shared_api_client import SharedApiClient
from api_client.user_not_exists_exception import UserNotExistsException
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder


class LoginController(flask_restful.Resource):
	def __init__(self):
		self.parser = reqparse.RequestParser()
		self.shared_api_client = SharedApiClient()

	def post(self):
		try:
			self.parser.add_argument('username', required=True, help="username cannot be blank!")
			self.parser.add_argument('password', required=True, help="password cannot be blank!")
			args = self.parser.parse_args()
			response = self.shared_api_client.login(args["username"],args["password"])
			if not response:
				return ErrorHandler.create_error_response("You don't have authorization", 401)

			return ResponseBuilder.build_response(response, 201)

		except RequestException as e:
			return ErrorHandler.create_error_response(str(e), 500)

		except BadRequest as ex:
			return ErrorHandler.create_error_response("Fields username and password are mandatory", 400)

		except UserNotExistsException as e:
			return ErrorHandler.create_error_response(str(e), 409)

