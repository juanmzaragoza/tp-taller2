import json
import flask_restful
from flask import request
from models.reaction import ReactionModel
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from api_client.db_connection_error import DBConnectionError
from errors_exceptions.data_version_exception import DataVersionException
from errors_exceptions.no_reaction_found_exception import NoReactionFoundException
from errors_exceptions.user_mismatch_exception import UserMismatchException
from auth_service import login_required, validate_sender
from models.user_activity import UserActivityModel

class ReactionDetailController(flask_restful.Resource):
	
	@login_required
	def delete(self, reaction_id):
		try:
			self._validate_author(reaction_id)
			reaction = ReactionModel.remove_reaction(reaction_id)
			UserActivityModel.log_reaction_activity(reaction["user_id"], reaction["storie_id"], reaction["reaction"], "DELETE")
			return self._get_reactions_response(reaction)
		except NoReactionFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except UserMismatchException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
					 		 
	def _get_reactions_response(self, reactions):
		return reactions

	def _validate_author(self, reaction_id):
		reaction = ReactionModel.get_reaction_with_id(reaction_id)
		author_id = reaction.get('user_id')
		validate_sender(author_id)
