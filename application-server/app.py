import flask
import flask_restful
from flask import request

from controllers.be_friend_controller import BeFriendController
from controllers.be_friend_detail_controller import BeFriendDetailController
from controllers.comment_controller import CommentController
from controllers.comment_detail_controller import CommentDetailController
from controllers.friend_controller import FriendController
from controllers.friend_request_controller import FriendRequestController
from controllers.friend_request_detail_controller import FriendRequestDetailController
from controllers.login_controller import LoginController
from controllers.notification_controller import NotificationsController
from controllers.ping_controller import PingController
from controllers.profile_controller import ProfileController
from controllers.reaction_controller import ReactionController
from controllers.reaction_detail_controller import ReactionDetailController
from controllers.request_counter_controller import RequestCounterController
from controllers.response_builder import ResponseBuilder
from controllers.stats_controller import StatsController
from controllers.storie_comment_controller import StorieCommentController
from controllers.storie_controller import StorieController
from controllers.storie_detail_controller import StorieDetailController
from controllers.user_controller import UserController
from controllers.user_detail_controller import UserDetailController
from controllers.userapp_controller import UserAppController
from request_middleware import RequestMiddleware

app = flask.Flask(__name__)
app.debug = True;
app.wsgi_app = RequestMiddleware(app.wsgi_app)

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
	api.add_resource(RequestCounterController, '/requests')

	# app endpoints
	api.add_resource(UserAppController, '/users/<string:user_id>')
	api.add_resource(StorieController, '/stories')
	api.add_resource(CommentController, '/stories/comments')
	api.add_resource(StorieCommentController, '/stories/<string:storie_id>/comments')
	api.add_resource(CommentDetailController, '/stories/comments/<string:comment_id>')
	api.add_resource(ReactionController, '/stories/reactions')
	api.add_resource(ReactionDetailController, '/stories/reactions/<string:reaction_id>')
	api.add_resource(StorieDetailController, '/stories/<string:id>')
	api.add_resource(BeFriendController, '/befriend')
	api.add_resource(BeFriendDetailController, '/befriend/<string:user_id>')
	api.add_resource(ProfileController, '/profiles/<string:user_id>')
	api.add_resource(FriendRequestController, '/befriend/requests')
	api.add_resource(FriendRequestDetailController, '/befriend/requests/<string:request_id>')
	api.add_resource(FriendController, '/friends/<string:id>')
	api.add_resource(NotificationsController, '/notification')

	if __name__ == "__main__":
    		app.run(host='0.0.0.0', port=5858,debug=True)

@app.before_request
def log_request_info():
    app.logger.debug('Request Headers: %s', request.headers)
    app.logger.debug('Request Body: %s', request.get_data())

@app.after_request
def log_request_output(response):
    app.logger.debug('Response Status: %s', response.status)
    app.logger.debug('Response Body: %s', response.get_data())
    return response