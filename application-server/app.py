import flask
import flask_restful
import os
from constants import MONGODB_USER, MONGODB_PASSWD

from controllers.db_controller import MongoController

from controllers.login_controller import LoginController
from controllers.user_controller import UserController
from controllers.user_detail_controller import UserDetailController

from controllers.ping_controller import PingController

app = flask.Flask(__name__)

with app.app_context():
	api = flask_restful.Api(app, prefix="/api/v1")

	class HelloWorld(flask_restful.Resource):
		def get(self):
			app.logger.error('%s logged in successfully', 'lalal')
			db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
			root = db.users.find_one({'user': 'root'})
			root_name = root.get('name')
			return {'hello': root_name}

	api.add_resource(HelloWorld, '/')

	# user's endpoints
	api.add_resource(LoginController, '/token')
	api.add_resource(UserController, '/user')
	api.add_resource(UserDetailController, '/user/<string:user_id>')

	# for shared-server endpoints
	api.add_resource(PingController, '/ping')
	
	if __name__ == "__main__":
    		app.run(host='0.0.0.0', port=5858,debug=True)
