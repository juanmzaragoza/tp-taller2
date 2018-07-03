import os
import pymongo
from api_client.db_connection_error import DBConnectionError
from pymongo import ReturnDocument

RETURN_DOCUMENT_AFTER = ReturnDocument.AFTER

class HelperMongoController():
	
	@staticmethod
	def get_mongodb_instance(user, passwd):
		try:
			conn=pymongo.MongoClient(os.environ['MONGO_URI'])
			conn.testdb.authenticate(user, passwd, mechanism = 'SCRAM-SHA-1')
			db = conn.testdb
			return db
		except pymongo.errors.ConnectionFailure as e:
			raise DBConnectionError(e)

	@staticmethod
	def drop_db():
		conn=pymongo.MongoClient(os.environ['MONGO_URI'])
		conn.drop_database('testdb')
