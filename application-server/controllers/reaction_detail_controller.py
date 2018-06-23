import json
import flask_restful
from flask import request
from models.reaction import ReactionModel
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from api_client.db_connection_error import DBConnectionError
from errors_exceptions.data_version_exception import DataVersionException
from errors_exceptions.no_reaction_found_exception import NoReactionFoundException
from auth_service import login_required
from models.user_activity import UserActivityModel

class ReactionDetailController(flask_restful.Resource):
	
	def delete(self, reaction_id):
		try:
			 reaction = ReactionModel.remove_reaction(reaction_id)
			 UserActivityModel.log_reaction_activity(reaction["user_id"], reaction["storie_id"], reaction["reaction"], "DELETE")
			 return self._get_reactions_response(reaction)
		except NoReactionFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
					 		 
	def _get_reactions_response(self, reactions):
		return reactions
