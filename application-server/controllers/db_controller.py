import pymongo
import os
from api_client.db_connection_error import DBConnectionError

class MongoController():
	@staticmethod
	def get_mongodb_instance(user, passwd):
		try:
			conn=pymongo.MongoClient(os.environ['MONGO_URI'])
			conn.appserverdb.authenticate(user, passwd, mechanism = 'SCRAM-SHA-1')
			db = conn.appserverdb
			return db
		except pymongo.errors.ConnectionFailure as e:
			raise DBConnectionError(e)
