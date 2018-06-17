import json
import flask_restful
from flask import request
from flask_restful import reqparse
from models.reaction import ReactionModel
from werkzeug.exceptions import BadRequest
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from api_client.db_connection_error import DBConnectionError
from errors_exceptions.no_storie_found_exception import NoStorieFoundException

class ReactionController(flask_restful.Resource):
	
	def __init__(self):
		self.parser = reqparse.RequestParser(bundle_errors=True)
	
	def post(self):
		try:
			self.parser.add_argument('reaction', required=True, help="Field reaction is mandatory")
			self.parser.add_argument('user_id', required=True, help="Field user_id is mandatory")
			self.parser.add_argument('storie_id', required=True, help="Field storie_id is mandatory")

			args = self.parser.parse_args()
			reaction = ReactionModel.create_reaction(args)
			return reaction
			 
		except BadRequest as ex:
			return ErrorHandler.create_error_response("Fields reaction, user_id and storie_id are mandatory", 400)
		except NoStorieFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
