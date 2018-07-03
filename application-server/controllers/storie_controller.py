import flask_restful
from flask import request
from flask_restful import reqparse
from werkzeug.exceptions import BadRequest

from api_client.db_connection_error import DBConnectionError
from auth_service import login_required, validate_sender
from controllers.error_handler import ErrorHandler
from errors_exceptions.user_mismatch_exception import UserMismatchException
from api_client.shared_api_client import SharedApiClient
from models.storie import StorieModel
from models.user_activity import UserActivityModel

class StorieController(flask_restful.Resource):

	def __init__(self):
		self.parser = reqparse.RequestParser(bundle_errors=True)
		self.shared_api_client = SharedApiClient()

	@login_required
	def post(self):
		try:

			self.parser.add_argument('title', required=True, help="Field title is mandatory")
			self.parser.add_argument('location', required=True, help="Field location is mandatory")
			self.parser.add_argument('visibility', required=True, help="Field visibility is mandatory")
			self.parser.add_argument('multimedia', required=True, help="Field multimedia is mandatory")
			self.parser.add_argument('story_type', required=True, help="Field story_type is mandatory")
			self.parser.add_argument('user_id', required=True, help="Field user_id is mandatory")

			args = self.parser.parse_args()
			validate_sender(args.get("user_id"))

			storie = self._create_user_storie_request(request)

			UserActivityModel.log_storie_activity(storie["user_id"], storie["_id"], "ADD")
			self._create_multimedia_file(args.get('title'), args.get('multimedia'))
			return storie

		except BadRequest as ex:
			return ErrorHandler.create_error_response("Fields title, location, visibility, multimedia, user_id and story_type are mandatory", 400)
		except UserMismatchException as e:
			return ErrorHandler.create_error_response(str(e), 409)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)

	def _create_user_storie_request(self, request):
		body = request.get_json()
		storie = StorieModel.create_user_storie(body)
		return storie

	def _create_multimedia_file(self, filename, multimedia):
		self.shared_api_client.fileCreate(filename, multimedia)
