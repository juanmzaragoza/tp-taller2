from flask import jsonify, make_response

class ResponseBuilder:
  @staticmethod
  def build_response(response, status_code=200):
    return make_response(jsonify(response), status_code)