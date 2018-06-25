import json
import flask_restful
from flask import request
from flask_restful import reqparse
from models.comment import CommentModel
from werkzeug.exceptions import BadRequest
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from api_client.db_connection_error import DBConnectionError
from errors_exceptions.data_version_exception import DataVersionException
from errors_exceptions.no_comment_found_exception import NoCommentFoundException
from errors_exceptions.no_user_data_found_exception import NoUserDataFoundException
from errors_exceptions.user_mismatch_exception import UserMismatchException
from auth_service import login_required, validate_sender
from models.user_activity import UserActivityModel

class CommentDetailController(flask_restful.Resource):
	
	def __init__(self):
		self.parser = reqparse.RequestParser(bundle_errors=True)
		
	@login_required
	def delete(self, comment_id):
		try:
			self._validate_author(comment_id)
			comment = CommentModel.remove_comment(comment_id)
			UserActivityModel.log_comment_activity(comment["user_id"], comment["storie_id"], "DELETE")
			return self._get_comments_response(comment)
		except NoCommentFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except UserMismatchException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
	
	@login_required
	def put(self, comment_id):
		try:
			self.parser.add_argument('_id', required=True, help="Field id is mandatory")
			self.parser.add_argument('_rev', required=True, help="Field rev is mandatory")
			self.parser.add_argument('storie_id', required=True, help="Field storie_id is mandatory")
			self.parser.add_argument('user_id', required=True, help="Field user_id is mandatory")
			self.parser.add_argument('message', required=True, help="Field message is mandatory")

			self._validate_author(comment_id)
			args = self.parser.parse_args()
			
			body = json.loads(request.data.decode('utf-8'))
			comment = CommentModel.update_comment(comment_id, body)
			UserActivityModel.log_comment_activity(comment["user_id"], comment["storie_id"], "EDIT")

			return ResponseBuilder.build_response(comment, 200)
		
		except BadRequest as ex:
			return ErrorHandler.create_error_response("Fields id, rev, storie_id, user_id and message are mandatory", 400)
		except NoUserDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except NoCommentFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DataVersionException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except UserMismatchException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
					 		 
	def _get_comments_response(self, comments):
		return comments

	def _validate_author(self, comment_id):
		comment = CommentModel.get_comment_with_id(comment_id)
		author_id = comment.get('user_id')
		validate_sender(author_id)
