import unittest
import unittest.mock as mock
from unittest.mock import patch
import requests
import json
import sys
import app

class TestUserApi(unittest.TestCase):

    def setUp(self):
        self.app = app.app.test_client()

    def __make_post_request(self, data):
        url = "api/v1/user"
        headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
        response = self.app.post(url, data=json.dumps(data), headers=headers)
        return response

    def __get_response_data(self, response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))


    def test_missing_username_should_status_400(self):
        # set up
        data = {"password": "123", "id": 1}
        # execution
        response = self.__make_post_request(data)
        #assertions
        self.assertEqual(response.status_code,400)
        response_data = self.__get_response_data(response)
        self.assertIn("code", response_data)
        self.assertEqual(400,response_data["code"])
        self.assertIn("message", response_data)


    def test_missing_password_should_status_400(self):
        # set up
        data = {"username": "jj", "id": 1}
        # execution
        response = self.__make_post_request(data)
        #assertions
        self.assertEqual(response.status_code,400)
        response_data = self.__get_response_data(response)
        self.assertIn("code", response_data)
        self.assertEqual(400,response_data["code"])
        self.assertIn("message", response_data)

    def test_missing_id_should_status_400(self):
        # set up
        data = {"username": "jj", "password": "123"}
        # execution
        response = self.__make_post_request(data)
        #assertions
        self.assertEqual(response.status_code,400)
        response_data = self.__get_response_data(response)
        self.assertIn("code", response_data)
        self.assertEqual(400,response_data["code"])
        self.assertIn("message", response_data)
        
    @patch('api_client.shared_api_client.requests.post')
    def test_user_create_successfull(self, mock_post):
        # set up
        data = {"id": 1, "username": "jmz", "password": "1234"}
        mock_post.return_value.status_code = 200
        mock_post.return_value.json.return_value = {
            "metadata": {
                "version": "v1"
            },
            "user": {
                "applicationOwner": "app1",
                "id": "3BCB3CD2-7FF1-4F10-BAE5-0B14446CF365",
                "role": "default",
                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7fSwiaWF0IjoxNTIzNzQwNzg5LCJleHAiOjE1MjM3NDQzODl9.uetJ4nXdYe2RNJnZkLLyTUTFA5ngrIGfP990U5DQUq0",
                "username": "jmz"
            }
        }
        # execution
        response = self.__make_post_request(data)
        #assertions
        self.assertEqual(response.status_code,200)
        response_data = self.__get_response_data(response)

        self.assertIn("metadata", response_data)
        self.assertIn("version", response_data["metadata"])
        self.assertEqual(response_data["metadata"]["version"], "v1")

        self.assertIn("user", response_data)
        self.assertIn("id", response_data["user"])
        self.assertIn("applicationOwner", response_data["user"])
        self.assertIn("role", response_data["user"])
        self.assertIn("token", response_data["user"])
        self.assertIn("username", response_data["user"])

        self.assertEqual(response_data["user"]["applicationOwner"], "app1")
        self.assertEqual(response_data["user"]["id"], "3BCB3CD2-7FF1-4F10-BAE5-0B14446CF365")
        self.assertEqual(response_data["user"]["role"], "default")
        self.assertEqual(response_data["user"]["token"], "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7fSwiaWF0IjoxNTIzNzQwNzg5LCJleHAiOjE1MjM3NDQzODl9.uetJ4nXdYe2RNJnZkLLyTUTFA5ngrIGfP990U5DQUq0")
        self.assertEqual(response_data["user"]["username"], "jmz")

    @patch('api_client.shared_api_client.requests.post')
    def test_remote_server_down_should_status_500(self, mock_post):
        # set up
        data = {"id":"invalid_id", "username": "invalid_username", "password": "valid_password"}
        mock_post.return_value.status_code = 500
        # execution
        response = self.__make_post_request(data)
        #assertions
        self.assertEqual(response.status_code,500)
        response_data = self.__get_response_data(response)
        self.assertIn("code", response_data)
        self.assertEqual(500,response_data["code"])
        self.assertIn("message", response_data)
        self.assertEqual(response_data["message"], "internal error")
