import unittest
import unittest.mock as mock
from unittest.mock import patch
import requests
import json
import sys
import app
    
class TestFlaskApiUsingRequests(unittest.TestCase):
	def setUp(self):
		self.app = app.app.test_client()

	def __make_get_request(self):
		url = "api/v1"
		headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
		response = self.app.get(url, headers=headers)
		return response
		
	def __get_response_data(self, response):
		return json.loads(response.get_data().decode(sys.getdefaultencoding()))

	def test_hello_world(self):
		response = self.__make_get_request()
		self.assertEqual(response.status_code,200)
		response_data = self.__get_response_data(response)
		self.assertIn("hello", response_data)
		self.assertEqual("root",response_data["hello"])

if __name__ == "__main__":
    unittest.main()
