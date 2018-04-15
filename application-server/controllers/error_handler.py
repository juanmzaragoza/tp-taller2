from flask import jsonify, make_response

class ErrorHandler:
  @staticmethod
  def create_error_response(message, status_code):

    response = {}
    response["code"] = status_code
    response["message"] = message

    return make_response(
      jsonify(response),
      status_code
    )