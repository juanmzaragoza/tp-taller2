import flask
import flask_restful
from constants import MONGODB_USER, MONGODB_PASSWD

from controllers.db_controller import MongoController
from controllers.login_controller import LoginController
from controllers.user_controller import UserController
from controllers.user_detail_controller import UserDetailController

from controllers.ping_controller import PingController
from controllers.profile_controller import ProfileController
from controllers.friend_controller import FriendController
from controllers.friend_request_controller import FriendRequestController
from controllers.friend_request_detail_controller import FriendRequestDetailController
from controllers.be_friend_controller import BeFriendController
from controllers.be_friend_detail_controller import BeFriendDetailController
from controllers.response_builder import ResponseBuilder

app = flask.Flask(__name__)

with app.app_context():
	api = flask_restful.Api(app, prefix="/api/v1")

	class HelloWorld(flask_restful.Resource):
		def get(self):
			response = {'hello': "appServer"}
			return ResponseBuilder.build_response(response, 200)

	api.add_resource(HelloWorld, '/')

	# user's endpoints
	api.add_resource(LoginController, '/token')
	api.add_resource(UserController, '/user')
	api.add_resource(UserDetailController, '/user/<string:user_id>')

	# for shared-server endpoints
	api.add_resource(PingController, '/ping')
	
	# app endpoints
	api.add_resource(BeFriendController, '/befriend')
	api.add_resource(BeFriendDetailController, '/befriend/<string:user_id>')
	api.add_resource(ProfileController, '/profiles/<string:user_id>')
	api.add_resource(FriendRequestController, '/befriend/requests')
	api.add_resource(FriendRequestDetailController, '/befriend/requests/<string:request_id>')
	api.add_resource(FriendController, '/friends/<string:user_id>')
	
	if __name__ == "__main__":
    		app.run(host='0.0.0.0', port=5858,debug=True)
