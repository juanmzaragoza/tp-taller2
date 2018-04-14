import flask
import requests
import json
from .request_exception import RequestException
from constants import SHARED_SERVER_URL, APPLICATION_OWNER

app = flask.Flask(__name__)

class SharedApiClient():

	def __init__(self):
		self.url = SHARED_SERVER_URL
		self.headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}

	def login(self, username, password):
		# app.logger.error('url: %s', self.url)
		try:
			data = {'username': username,'password': password}
			url = self.url + '/token'

			response = requests.post(url, data=json.dumps(data), headers=self.headers)

			if (response.status_code == 500):
				raise RequestException("shared server error")

			if (response.status_code == 401):
				return False

			return response.json()
		except:
			raise RequestException("internal error")

	def __get_response_data(self, response):
		parsed_response = response.json()
		response_data = {
			"metadata": {
				"version": parsed_response['metadata']['version']
			},
			"token": {
				"expiresAt": parsed_response['token']['expiresAt'],
				"token": parsed_response['token']['token']
			}
		}
		return response_data

	def userCreate(self, fbId, username, password):
		# app.logger.error('url: %s', self.url)
		try:
			data = {'id': fbId, 'password': password, 'applicationOwner': APPLICATION_OWNER}
			url = self.url + '/user'

			response = requests.post(url, data=json.dumps(data), headers=self.headers)

			if (response.status_code == 500):
				raise RequestException("shared server error")

			if (response.status_code == 401):
				return False

			return response.json()
		except:
			raise RequestException("internal error")