import flask
import flask_restful
import pymongo
from constants import MONGODB_LOCAL_URI, MONGODB_DOCKER_URI

class MongoController(flask_restful.Resource):
	@staticmethod
	def get_mongodb_instance(user, passwd):
		try:
			conn=pymongo.MongoClient(MONGODB_LOCAL_URI)
			conn.appserverdb.authenticate(user, passwd, mechanism = 'SCRAM-SHA-1')
			db = conn.appserverdb
			return db
		except pymongo.errors.ConnectionFailure as e:
			raise Exception('Could not connect to MongoDB %s' % e)
