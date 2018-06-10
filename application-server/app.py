import flask
import flask_restful
from constants import MONGODB_USER, MONGODB_PASSWD

from controllers.db_controller import MongoController
from controllers.login_controller import LoginController
from controllers.user_controller import UserController
from controllers.user_detail_controller import UserDetailController
from controllers.userapp_controller import UserAppController
from controllers.ping_controller import PingController
from controllers.comment_controller import CommentController
from controllers.comment_detail_controller import CommentDetailController
from controllers.reaction_controller import ReactionController
from controllers.reaction_detail_controller import ReactionDetailController
from controllers.stats_controller import StatsController
from controllers.profile_controller import ProfileController
from controllers.storie_controller import StorieController
from controllers.storie_detail_controller import StorieDetailController
from controllers.friend_controller import FriendController
from controllers.friend_detail_controller import FriendDetailController
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
	api.add_resource(StatsController, '/stats')
	
	# app endpoints
	api.add_resource(UserAppController, '/users/<string:user_id>')
	api.add_resource(StorieController, '/stories')
	api.add_resource(CommentController, '/stories/comments')
	api.add_resource(CommentDetailController, '/stories/comments/<string:comment_id>')
	api.add_resource(ReactionController, '/stories/reactions')
	api.add_resource(ReactionDetailController, '/stories/reactions/<string:reaction_id>')
	api.add_resource(StorieDetailController, '/stories/<string:id>')
	api.add_resource(BeFriendController, '/befriend')
	api.add_resource(BeFriendDetailController, '/befriend/<string:user_id>')
	api.add_resource(ProfileController, '/profiles/<string:user_id>')
	api.add_resource(FriendRequestController, '/befriend/requests')
	api.add_resource(FriendRequestDetailController, '/befriend/requests/<string:request_id>')
	api.add_resource(FriendController, '/friends/<string:user_id>')
	api.add_resource(FriendDetailController, '/friends/<string:friend_id>')
	
	if __name__ == "__main__":
    		app.run(host='0.0.0.0', port=5858,debug=True)
