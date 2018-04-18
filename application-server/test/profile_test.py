import unittest
import unittest.mock as mock
from unittest.mock import patch
import requests
import json
import sys
import app

class TestFlaskApi(unittest.TestCase):

    def setUp(self):
        self.app = app.app.test_client()

    def __make_get_request(self, userId):
        url = "api/v1/profile/%d" % userId
        headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
        response = self.app.get(url, headers=headers)
        return response

    def __get_response_data(self, response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))
		
    def test_successful_profile_data_should_return_status_200(self):
        # execution
        userId = 1
        response = self.__make_get_request(userId)
        #assertions
        self.assertEqual(response.status_code,200)
        response_data = self.__get_response_data(response)
        self.assertIn("name", response_data)
        self.assertEqual("Pepe",response_data["name"])
        self.assertIn("last_name", response_data)
        self.assertEqual("Gomez",response_data["last_name"])
        self.assertIn("email", response_data)
        self.assertEqual("pepe@email.com",response_data["email"])
        self.assertIn("profile_picture", response_data)
        self.assertIn("stories", response_data)

    def test_profile_no_data_found_shouldreturn_status_404(self):
        userId = 1e10
        response = self.__make_get_request(userId)
        self.assertEqual(response.status_code,404)
        response_data = self.__get_response_data(response)
        self.assertIn("code", response_data)
        self.assertEqual(404,response_data["code"])
        self.assertIn("message", response_data)
        
if __name__ == "__main__":
    unittest.main()
