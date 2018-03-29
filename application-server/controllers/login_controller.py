import flask_restful
from flask_restful import reqparse

class LoginController(flask_restful.Resource):
	def __init__(self):
		self.parser = reqparse.RequestParser()


	def get(self):
		return "llalala"

	def post(self):
		self.parser.add_argument('username', required=True, help="username cannot be blank!")
		self.parser.add_argument('password', required=True, help="password cannot be blank!")

		args = self.parser.parse_args()
		# return {
		# 	"password": args['password']
		# }

		response_data =  {
			"metadata": {
				"version": "string"
			},
			"token": {
				"expiresAt": 0,
				"token": "string"
			}
		}

		return response_data, 201


