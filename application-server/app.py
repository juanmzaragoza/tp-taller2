import flask
import flask_restful
from flask_pymongo import PyMongo

app = flask.Flask(__name__)
with app.app_context():

	app.config['MONGO_DBNAME'] = 'test'
	app.config['MONGO_URI'] = 'mongodb://localhost:27017/test'
	#app.config['MONGO_URI'] = 'mongodb://mongo:27017/test';#for docker test

	api = flask_restful.Api(app)
	mongo = PyMongo(app)
	doc = mongo.db.test.insert({'user':'Jose'})

	class HelloWorld(flask_restful.Resource):
		def get(self):
			root = mongo.db.test.find_one_or_404({'user': 'Jose'})
			root_name = root.get('name')
			return {'hello': root_name}

	api.add_resource(HelloWorld, '/')

	if __name__ == "__main__":
    		app.run(host='0.0.0.0', port=5858,debug=True)
