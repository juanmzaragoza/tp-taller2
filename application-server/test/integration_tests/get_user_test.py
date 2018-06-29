import json
import sys
import unittest
from unittest.mock import patch

import app


class TestFlaskGetUserApi(unittest.TestCase):

    def setUp(self):
        self.app = app.app.test_client()

    def __make_get_request(self, userId):
        url = "api/v1/user/"+str(userId)
        headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
        response = self.app.get(url, headers=headers)
        return response

    def __get_response_data(self, response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))


    def test_missing_userid_should_return_status_404(self):
        # set up
        userId = ""
        # execution
        response = self.__make_get_request(userId)
        #assertions
        self.assertEqual(response.status_code,404)

    @patch('api_client.shared_api_client.requests.get')
    def test_user_not_exists_must_return_404(self, mock_post):
        # set up
        userId = 1
        mock_post.return_value.status_code = 404
        mock_post.return_value.json.return_value = {
            "code": 404,
            "message": "User doesn't exists"
        }
        # execution
        response = self.__make_get_request(userId)
        #assertions
        self.assertEqual(response.status_code,404)
        response_data = self.__get_response_data(response)
        self.assertIn("code", response_data)
        self.assertEqual(404,response_data["code"])
        self.assertIn("message", response_data)
        self.assertEqual(response_data["message"], "User doesn't exists")
