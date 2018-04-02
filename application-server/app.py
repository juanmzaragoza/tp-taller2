import flask
import flask_restful
from flask_pymongo import PyMongo
import os
from controllers.login_controller import LoginController

app = flask.Flask(__name__)

with app.app_context():
	app.config['MONGO_DBNAME'] = 'application-server'
	app.config['MONGO_URI'] = os.environ['MONGO_URI']
	#set env var MONGO_URI 'mongodb://localhost:27017/test' for local test
	#set env var MONGO_URI 'mongodb://mongo:27017/test' for docker test

	api = flask_restful.Api(app)
	mongo = PyMongo(app)
	doc = mongo.db.test.insert({'user':'root', 'name':'Jose'})

	class HelloWorld(flask_restful.Resource):
		def get(self):
			root = mongo.db.test.find_one_or_404({'user': 'root'})
			root_name = root.get('name')
			return {'hello': root_name}

	api.add_resource(HelloWorld, '/')
	api.add_resource(LoginController, '/token')

	if __name__ == "__main__":
    		app.run(host='0.0.0.0', port=5858,debug=True)
