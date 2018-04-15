import flask_restful
from controllers.response_builder import ResponseBuilder

import json

class PingController(flask_restful.Resource):
	
	def get(self):
		response = self.__ping_successful_response()
		return ResponseBuilder.build_response(response, 200)
		
	def __ping_successful_response(self):
		return {
			"status": "active"
		}
