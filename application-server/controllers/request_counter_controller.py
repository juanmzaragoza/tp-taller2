import json
import flask_restful
from flask import request
from controllers.error_handler import ErrorHandler
from models.request_counter import RequestCounterModel
from controllers.response_builder import ResponseBuilder
from api_client.db_connection_error import DBConnectionError

class RequestCounterController(flask_restful.Resource):
	
	@staticmethod
	def save_new_request():
		RequestCounterModel.inc_requests()
	
	def get(self):
		try:
			requests = RequestCounterModel.get_requests()
			return ResponseBuilder.build_response(requests, 200)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
