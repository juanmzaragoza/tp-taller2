from controllers.request_counter_controller import RequestCounterController

class RequestMiddleware(object):

	def __init__(self, app):
		self.app = app

	def __call__(self, environ, start_response):
		RequestCounterController.save_new_request()
		return self.app(environ, start_response)