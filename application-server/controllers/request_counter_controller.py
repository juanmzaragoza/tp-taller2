import flask_restful
from flask import request

from api_client.db_connection_error import DBConnectionError
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from models.request_counter import RequestCounterModel


class RequestCounterController(flask_restful.Resource):

	@staticmethod
	def save_new_request():
		RequestCounterModel.inc_requests()

	def get(self):
		try:
			fromHour = request.args.get('from')
			toHour = request.args.get('to')
			requests = RequestCounterModel.get_requests(fromHour = fromHour, toHour = toHour)
			return ResponseBuilder.build_response(requests, 200)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
