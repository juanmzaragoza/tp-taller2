import flask_restful
from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.response_builder import ResponseBuilder
from controllers.error_handler import ErrorHandler
from api_client.no_data_found_exception import NoDataFoundException
from api_client.db_connection_error import DBConnectionError

class ProfileController(flask_restful.Resource):
	
	def get(self,user_id):
		try:
			db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
			try:	
				response = self.__get_user_data(db,user_id)
			except NoDataFoundException as e:
				return ErrorHandler.create_error_response(str(e), 404)
			return ResponseBuilder.build_response(response, 200)
		except DBConnectionError as e:
			return ErrorHandler.create_error_response(str(e), 500)
		
	def __get_user_data(self,db,user_id):
		profile = db.profiles.find_one({'user_id': user_id})
		#stories = self.__get_user_stories(db, user_id)
		stories = {}
		if profile == None:
			raise NoDataFoundException
			
		return {	
			'last_name' : profile.get('last_name'),
			'name': profile.get('name'),
			'email': profile.get('email'),
			'profile_picture': '',
			'stories': stories
		}
	
