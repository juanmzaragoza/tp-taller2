import flask
import flask_restful
from constants import MONGODB_USER, MONGODB_PASSWD

from controllers.db_controller import MongoController
from controllers.login_controller import LoginController
from controllers.user_controller import UserController
from controllers.user_detail_controller import UserDetailController

from controllers.ping_controller import PingController
from controllers.profile_controller import ProfileController

app = flask.Flask(__name__)

with app.app_context():
	api = flask_restful.Api(app, prefix="/api/v1")

	class HelloWorld(flask_restful.Resource):
		def get(self):
			db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
			root = db.users.find_one({'user': 'root'})
			root_user = root.get('user')
			return {'hello': root_user}

	api.add_resource(HelloWorld, '/')

	# user's endpoints
	api.add_resource(LoginController, '/token')
	api.add_resource(UserController, '/user')
	api.add_resource(UserDetailController, '/user/<string:user_id>')

	# for shared-server endpoints
	api.add_resource(PingController, '/ping')
	api.add_resource(ProfileController, '/profile/<int:user_id>')

	if __name__ == "__main__":
    		app.run(host='0.0.0.0', port=5858,debug=True)
