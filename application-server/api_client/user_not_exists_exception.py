class UserNotExistsException(Exception):
	def __init__(self):
		super(UserNotExistsException, self).__init__("FB User Not Exists")