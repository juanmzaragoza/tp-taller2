import unittest
import unittest.mock as mock
from unittest.mock import patch
import requests
import json
import sys

try:
    import app.app as app #for docker test
except ImportError:
    import app #for local test

class TestFlaskApi(unittest.TestCase):

    def setUp(self):
        self.app = app.app.test_client()

    def __make_post_request(self, data):
        url = "/token"
        headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
        response = self.app.post(url, data=json.dumps(data), headers=headers)
        return response

    def __get_response_data(self, response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))


    def test_missing_username_should_status_400(self):
        # set up
        data = {"password": "beeeee"}
        # execution
        response = self.__make_post_request(data)
        #assertions
        self.assertEqual(response.status_code,400)
        response_data = self.__get_response_data(response)
        self.assertIn("code", response_data)
        self.assertEqual(0,response_data["code"])
        self.assertIn("message", response_data)


    def test_missing_password_should_status_400(self):
        # set up
        data = {"username": "bob"}
        # execution
        response = self.__make_post_request(data)
        #assertions
        self.assertEqual(response.status_code,400)
        response_data = self.__get_response_data(response)
        self.assertIn("code", response_data)
        self.assertEqual(0,response_data["code"])
        self.assertIn("message", response_data)
        
    @patch('api_client.shared_api_client.requests.post')
    def test_token(self, mock_post):
        # set up
        data = {"username": "Erik", "password": "Erik"}
        mock_post.return_value.status_code = 201
        mock_post.return_value.json.return_value = {
            "metadata": {
                "version": "v1"
            },
            "token": {
                "expiresAt": 3600,
                "token": "string"
            }
        }
        # execution
        response = self.__make_post_request(data)
        #assertions
        self.assertEqual(response.status_code,201)
        response_data = self.__get_response_data(response)
        self.assertIn("metadata", response_data)
        self.assertIn("version", response_data["metadata"])
        self.assertEqual(response_data["metadata"]["version"], "v1")
        self.assertIn("token", response_data)
        self.assertIn("expiresAt", response_data["token"])
        self.assertIn("token", response_data["token"])
        self.assertEqual(response_data["token"]["expiresAt"], 3600)

    @patch('api_client.shared_api_client.requests.post')
    def test_invalid_user_should_status_401(self, mock_post):
        # set up
        data = {"username": "invalid_username", "password": "valid_password"}
        mock_post.return_value.status_code = 401
        # execution
        response = self.__make_post_request(data)
        #assertions
        self.assertEqual(response.status_code,401)
        response_data = self.__get_response_data(response)
        self.assertIn("code", response_data)
        self.assertEqual(0,response_data["code"])
        self.assertIn("message", response_data)
        self.assertEqual(response_data["message"], "invalid")

    @patch('api_client.shared_api_client.requests.post')
    def test_invalid_password_should_status_401(self, mock_post):
        # set up
        data = {"username": "valid_username", "password": "invalid_password"}
        mock_post.return_value.status_code = 401
        # execution
        response = self.__make_post_request(data)
        #assertions
        self.assertEqual(response.status_code,401)
        response_data = self.__get_response_data(response)
        self.assertIn("code", response_data)
        self.assertEqual(0,response_data["code"])
        self.assertIn("message", response_data)
        self.assertEqual(response_data["message"], "invalid")

    @patch('api_client.shared_api_client.requests.post')
    def test_remote_server_down_should_status_500(self, mock_post):
        # set up
        data = {"username": "valid_username", "password": "invalid_password"}
        mock_post.return_value.status_code = 500
        # execution
        response = self.__make_post_request(data)
        #assertions
        self.assertEqual(response.status_code,500)
        response_data = self.__get_response_data(response)
        self.assertIn("code", response_data)
        self.assertEqual(0,response_data["code"])
        self.assertIn("message", response_data)
        self.assertEqual(response_data["message"], "internal error")


if __name__ == "__main__":
    unittest.main()
