class DBConnectionError(Exception):
	def __init__(self, msg):
		super(DBConnectionError, self).__init__('Could not connect to MongoDB %s' % msg)
