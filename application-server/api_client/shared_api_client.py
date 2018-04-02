import requests
import json
from .request_exception import RequestException

class SharedApiClient():

	def __init__(self):
		#self.url = 'http://172.17.0.1:8081/api/v1/token'
		self.url = 'http://0.0.0.0:8081/api/v1/token'

	def login(self, username, password):
		try:
			data = {'username': username,'password': password}
			headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}

			response = requests.post(self.url, data=json.dumps(data), headers=headers)

			if (response.status_code == 500):
				raise RequestException("shared server error")

			if (response.status_code == 401):
				return False

			response_data = self.__get_response_data(response)
			return response_data
		except:
			raise RequestException("internal error")

	def __get_response_data(self, response):
		response_data = {
			"metadata": {
				"version": "string"
			},
			"token": {
				"expiresAt": 0,
				"token": response.json()['token']
			}
		}
		return response_data
