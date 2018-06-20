import flask_restful
from controllers.error_handler import ErrorHandler
from controllers.response_builder import ResponseBuilder
from api_client.db_connection_error import DBConnectionError
from models.user_data import UserDataModel
from models.storie import StorieModel
from models.comment import CommentModel

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
			"numUsers": self._get_num_users(),
			"numUsersActiveToday": self._get_num_users_today(),
			"numStories": self._get_num_stories(),
			"numStoriesToday": self._get_num_stories_today(),
			"numFastStoriesToday": self._get_num_fast_stories_today(),
			"numUsersMessages": self._get_num_user_messages(),
			"numUsersMessagesToday": self._get_num_user_messages_today(),
			"numAcceptedContactsToday": self._get_num_accepted_contacts_today()
		}
		
		return ResponseBuilder.get_build_response(stats, 'stats', 200)


	def _get_num_users(self):
		return UserDataModel.count_all_users()

	def _get_num_users_today(self):
		return 1

	def _get_num_stories(self):
		return StorieModel.count_stories()

	def _get_num_stories_today(self):
		return StorieModel.count_today_stories()

	def _get_num_fast_stories_today(self):
		return StorieModel.count_today_stories('fast')

	def _get_num_user_messages(self):
		return CommentModel.count_comments()

	def _get_num_user_messages_today(self):
		return CommentModel.count_today_comments()

	def _get_num_accepted_contacts_today(self):
		return 7