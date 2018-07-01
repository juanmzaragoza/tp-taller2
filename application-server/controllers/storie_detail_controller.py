import json

import flask
import flask_restful
from flask import request
from flask_restful import reqparse
from werkzeug.exceptions import BadRequest

from api_client.db_connection_error import DBConnectionError
from auth_service import login_required, validate_sender
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from errors_exceptions.data_version_exception import DataVersionException
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.no_storie_found_exception import NoStorieFoundException
from errors_exceptions.no_user_data_found_exception import NoUserDataFoundException
from errors_exceptions.user_mismatch_exception import UserMismatchException
from models.storie import StorieModel
from models.user_activity import UserActivityModel

app = flask.Flask(__name__)

class StorieDetailController(flask_restful.Resource):

	def __init__(self):
		self.parser = reqparse.RequestParser(bundle_errors=True)

	@login_required
	def get(self, id):
		try:
			return self._get_storie_by_id(id)
		except NoUserDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	def _get_storie_by_id(self, id):
		storie_type = request.args.get('story_type')
		if (storie_type not in ['normal', 'fast']):
			storie_type = 'normal'
		stories = StorieModel.get_stories(id, storie_type)
		return self._create_get_stories_response(stories)

	@login_required
	def put(self, id):
		try:
			self.parser.add_argument('_id', required=True, help="Field id is mandatory")
			self.parser.add_argument('_rev', required=True, help="Field rev is mandatory")
			self.parser.add_argument('title', required=True, help="Field title is mandatory")
			self.parser.add_argument('location', required=True, help="Field location is mandatory")
			self.parser.add_argument('visibility', required=True, help="Field visibility is mandatory")
			self.parser.add_argument('multimedia', required=True, help="Field multimedia is mandatory")
			self.parser.add_argument('story_type', required=True, help="Field story_type is mandatory")
			self.parser.add_argument('user_id', required=True, help="Field user_id is mandatory")

			args = self.parser.parse_args()
			self._validate_author(id)
			body = json.loads(request.data.decode('utf-8'))

			storie = StorieModel.update_storie(id, body)
			UserActivityModel.log_storie_activity(storie["user_id"], storie["_id"], "EDIT")
			return ResponseBuilder.build_response(storie, 200)

		except BadRequest as ex:
			return ErrorHandler.create_error_response("Fields id, rev, title, location, user_id, visibility, multimedia and story_type are mandatory", 400)
		except NoStorieFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DataVersionException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except UserMismatchException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	@login_required
	def delete(self, id):
		try:
			self._validate_author(id)
			body = json.loads(request.data.decode('utf-8'))
			storie_user_id = body['user_id']
			storie = StorieModel.delete_storie(id, storie_user_id)
			UserActivityModel.log_storie_activity(storie["user_id"], storie["_id"], "DELETE")
			return ResponseBuilder.build_response(storie, 204)
		except NoStorieFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except UserMismatchException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	def get_stories_by_user_id(self, user_id):
		try:
			stories = StorieModel.get_profile_stories_by_user_id(user_id)
			return self._create_get_stories_response(stories)
		except NoDataFoundException as e:
			return ErrorHandler.create_error_response(str(e), 404)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	def _create_get_stories_response(self, stories):
		response = []
		for storie in stories:
			response.append(storie)
		return response

	def _validate_author(self, storie_id):
		storie = StorieModel.get_storie(storie_id)
		author_id = storie.get('user_id')
		validate_sender(author_id)
