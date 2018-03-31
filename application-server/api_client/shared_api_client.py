class SharedApiClient():

	def __init__(self):
		self.url = 'asd'

	def login(self, username, password):
		if ((username != "valid_username") or (password != "valid_password")):
			return False

		response_data =  {
			"metadata": {
				"version": "string"
			},
			"token": {
				"expiresAt": 0,
				"token": "string"
			}
		}
		return response_data
