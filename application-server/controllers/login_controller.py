import flask_restful
from flask_restful import reqparse
from api_client.shared_api_client import SharedApiClient

class LoginController(flask_restful.Resource):
	def __init__(self):
		self.parser = reqparse.RequestParser()
		self.shared_api_client = SharedApiClient()

	def get(self):
		return "llalala"

	def post(self):
		self.parser.add_argument('username', required=True, help="username cannot be blank!")
		self.parser.add_argument('password', required=True, help="password cannot be blank!")

		args = self.parser.parse_args()
		
		response = self.shared_api_client.login(args["username"],args["password"])
		if not response:
			return self.__unauthorized_response()

		return self.__authenticated_token_response(response)

	def __authenticated_token_response(self, response_data):
		return response_data, 201

	def __unauthorized_response(self):
		return {
			"message": "invalid"
		}, 401

