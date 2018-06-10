import flask_restful
import json
from flask import request
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.reaction import ReactionModel
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.data_version_exception import DataVersionException

class ReactionDetailController(flask_restful.Resource):
	
	def delete(self, reaction_id):
		try:
			 reaction = ReactionModel.remove_reaction(reaction_id)
			 return self._get_reactions_response(reaction)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
					 		 
	def _get_reactions_response(self, reactions):
		return reactions
