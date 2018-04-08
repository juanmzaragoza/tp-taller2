import flask_restful
from flask_restful import reqparse
from werkzeug.exceptions import BadRequest
from api_client.shared_api_client import SharedApiClient
from api_client.request_exception import RequestException

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
				return self.__unauthorized_response()
			
			return self.__authenticated_token_response(response)

		except RequestException as e:
			return self.__error_response(e)

		except BadRequest as ex:
			return self.__bad_request_response(ex)


	def __authenticated_token_response(self, response_data):
		return response_data, 201


	def __unauthorized_response(self):
		return {
			"code": 0,
			"message": "invalid"
		}, 401

	def __error_response(self, e):
		return {
			"code": 0,
			"message": str(e)
		}, 500

	def __bad_request_response(self, e):
		return {
			"code": 0,
			"message": str(e)
		}, 400

