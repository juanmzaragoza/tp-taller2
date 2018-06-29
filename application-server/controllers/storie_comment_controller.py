import json
import flask_restful
from flask import request
from flask_restful import reqparse
from models.comment import CommentModel
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError
from errors_exceptions.no_storie_found_exception import NoStorieFoundException
from auth_service import login_required

class StorieCommentController(flask_restful.Resource):
	
	def __init__(self):
		self.parser = reqparse.RequestParser(bundle_errors=True)
	
	@login_required
	def get(self, storie_id):
		try:
			comments = CommentModel.get_storie_comments(storie_id)
			return comments
		except NoStorieFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
