import flask_restful

class PingController(flask_restful.Resource):

	def get(self):	
		return self.__ping_response()

	def __ping_response(self):
		return {
			"status": "active"
		}, 200
