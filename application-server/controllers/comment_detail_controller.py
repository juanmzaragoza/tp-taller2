import flask_restful
import json
from flask import request
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from models.comment import CommentModel
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.data_version_exception import DataVersionException

class CommentDetailController(flask_restful.Resource):
	
	def delete(self, comment_id):
		try:
			 comment = CommentModel.remove_comment(comment_id)
			 return self._get_comments_response(comment)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
	
	def put(self, comment_id):
		try:
			body = json.loads(request.data.decode('utf-8'))
			comment = CommentModel.update_comment(comment_id, body)
			return ResponseBuilder.build_response(comment, 200)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DataVersionException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
					 		 
	def _get_comments_response(self, comments):
		return comments
