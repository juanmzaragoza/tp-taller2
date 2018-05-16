import flask_restful
from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.db_connection_error import DBConnectionError

class StatsController(flask_restful.Resource):
	
	def get(self):
		try:
			return self._get_appserver_stats_response()
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
		
	def _get_appserver_stats_response(self):
		stats = {
			"_id": "",
			"_rev": "",
			"numUsers": "148",
			"numUsersActiveToday": "78",
			"numStoriesToday": "48",
			"numFastStoriesToday": "28",
			"numStories": "448",
			"numUsersMessages": "208",
			"numUsersMessagesToday": "20",
			"numAcceptedContactsToday": "8"
		}
		
		return ResponseBuilder.get_build_response(stats, 'stats', 200)