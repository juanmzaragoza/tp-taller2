from flask import jsonify, make_response
class ResponseBuilder:
	@staticmethod
	def build_response(response, status_code=200):
		return make_response(jsonify(response), status_code)

	@staticmethod
	def get_build_response(response, tag, status_code=200):
		res = {"metadata": {"version": "1.0"},tag: response}
		return make_response(jsonify(res), status_code)
