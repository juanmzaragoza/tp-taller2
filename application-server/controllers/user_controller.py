import flask_restful
from flask_restful import reqparse
from werkzeug.exceptions import BadRequest

from api_client.shared_api_client import SharedApiClient
from api_client.request_exception import RequestException


from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler

class UserController(flask_restful.Resource):
	def __init__(self):
		self.parser = reqparse.RequestParser(bundle_errors=True)
		self.shared_api_client = SharedApiClient()

	def post(self):
		try:
			# save mandatory fields on app server
			self.parser.add_argument('id', required=True, help="Field id is mandatory") # fb id
			self.parser.add_argument('username', required=True, help="Field username is mandatory") # fb email
			self.parser.add_argument('password', required=True, help="Field password is mandatory")

			self.parser.add_argument('name', required=False)
			self.parser.add_argument('first_name', required=False)
			self.parser.add_argument('last_name', required=False)
			self.parser.add_argument('gender', required=False)
			self.parser.add_argument('picture', required=False)
			self.parser.add_argument('facebookAccount', required=False)

			args = self.parser.parse_args()
			response = self.shared_api_client.userCreate(args["id"],args["username"],args["password"])
			if not response:
				return ErrorHandler.create_error_response("You haven't authorization", 401)
			
			return ResponseBuilder.build_response(response, 200)

		except RequestException as e:
			return ErrorHandler.create_error_response(str(e), 500)

		except BadRequest as ex:
			return ErrorHandler.create_error_response("Fields id, username and password are mandatory", 400)

