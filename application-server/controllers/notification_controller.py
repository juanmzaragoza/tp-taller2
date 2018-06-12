import flask_restful
import json
from pyfcm import FCMNotification
from flask import Flask
import logging
from flask import request
from constants import FCM_SERVER_KEY
from controllers.response_builder import ResponseBuilder

class NotificationsController(flask_restful.Resource):

	def post(self):
		app = Flask(__name__)
		registration_id, message = self._get_notification_request_data(request)
		push_service = FCMNotification(api_key=FCM_SERVER_KEY)
		result = push_service.notify_single_device(registration_id=registration_id, message_title='Stories', message_body=message)
		if not result:
			return ErrorHandler.create_error_response("Error communicating with firebase cloud messaging", 40)
		return ResponseBuilder.build_response(result, 200)

	def _get_notification_request_data(self, request):
		body = request.get_json()
		return body['userFCMToken'], body['message']