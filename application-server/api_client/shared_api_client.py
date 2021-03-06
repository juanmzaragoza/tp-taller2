import sys
import flask
import requests
import json
import os

from .request_exception import RequestException
from api_client.user_not_exists_exception import UserNotExistsException
from constants import APPLICATION_OWNER, API_KEY
from flask import request

app = flask.Flask(__name__)

class SharedApiClient():

	def __init__(self):
		self.url = os.environ['SHARED_URI']
		self.headers = {
			'Content-type': 'application/json', 
			'Accept': 'text/plain',
			'api-key': API_KEY
		}

	def login(self, username, password):
		# app.logger.error('url: %s', self.url)
		try:

			data = {'username': username,'password': password}
			url = self.url + '/token'

			response = requests.post(url, data=json.dumps(data), headers=self.headers)

		except:
			print("Unexpected error:", sys.exc_info()[0])
			raise RequestException("internal error")

		if (response.status_code == 500):
			raise RequestException("shared server error")

		if (response.status_code == 409):
			# "The FB Token is Valid but user doesn't exists"
			raise UserNotExistsException()

		if ((response.status_code == 404) or (response.status_code == 401)):
			return False

		return response.json()

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
			data = {'id': fbId, 'username':username, 'password': password, 'applicationOwner': APPLICATION_OWNER}
			url = self.url + '/user'

			response = requests.post(url, data=json.dumps(data), headers=self.headers)

			if (response.status_code == 500):
				raise RequestException("shared server error")

			if (response.status_code == 401):
				return False

			return response.json()
		except:
			raise RequestException("internal error")

	def getUserById(self, userId):
		try:
			data = {'id': userId}
			url = self.url + '/user/' + userId

			response = requests.get(url, data=json.dumps(data), headers=self.headers)

			if (response.status_code == 500):
				raise RequestException("shared server error")

			if (response.status_code == 404):
				return False

			return response.json()
		except:
			raise RequestException("internal error")

	def fileCreate(self, filename, resource):
		try:
			data = {
				'filename': filename,
				'resource': resource
			}
			url = self.url + '/files'

			headers = self.headers
			headers['authorization'] = request.headers.get('authorization')
			response = requests.post(url, data=json.dumps(data), headers=headers)

			if (response.status_code == 500):
				raise RequestException("shared server error")

			if (response.status_code == 401):
				return False

			return response.json()
		except:
			raise RequestException("internal error")
