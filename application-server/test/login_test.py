import unittest
import unittest.mock as mock
import requests
import json
import sys
import app #for local test
#import app.app#for docker test

from api_client.shared_api_client import SharedApiClient


class TestFlaskApi(unittest.TestCase):

    def setUp(self):
        self.app = app.app.test_client()
        #self.app = app.app.app.test_client()#for docker test

    def __make_post_request(self, data):
        url = "/token"
        headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
        response = self.app.post(url, data=json.dumps(data), headers=headers)
        return response

    def __get_response_data(self, response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))

    # def test_e(self):
    #     url = "/token"
    #     data = {"username": "aaaaa", "password": "beeeee"}
    #     headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
    #     response = self.app.post(url, data=json.dumps(data), headers=headers)
    #     self.assertEqual(response.status_code,200)

    #     response_data = self.__get_response_data(response)
    #     self.assertEqual(
    #         response_data,
    #         {
    #             "password":  "beeeee"
    #         }
    #     )


    def test_missing_username_should_status_400(self):
        data = {"password": "beeeee"}
        response = self.__make_post_request(data)

        self.assertEqual(response.status_code,400)

        response_data = self.__get_response_data(response)
        self.assertIn("message", response_data)
        self.assertIn("username", response_data["message"])
        self.assertEqual(response_data["message"]["username"], "username cannot be blank!")

    def test_missing_password_should_status_400(self):
        data = {"username": "bob"}
        response = self.__make_post_request(data)
        
        self.assertEqual(response.status_code,400)

        response_data = self.__get_response_data(response)
        self.assertIn("message", response_data)
        self.assertIn("password", response_data["message"])
        self.assertEqual(response_data["message"]["password"], "password cannot be blank!")
        

    def test_token(self):
        data = {"username": "valid_username", "password": "valid_password"}
        response = self.__make_post_request(data)

        self.assertEqual(response.status_code,201)

        response_data = self.__get_response_data(response)
        self.assertIn("metadata", response_data)
        self.assertIn("version", response_data["metadata"])
        self.assertEqual(response_data["metadata"]["version"], "string")

        self.assertIn("token", response_data)
        self.assertIn("expiresAt", response_data["token"])
        self.assertIn("token", response_data["token"])
        self.assertEqual(response_data["token"]["expiresAt"], 0)
        self.assertEqual(response_data["token"]["token"], "string")

    #@mock.patch("api_client.shared_api_client")
    def test_invalid_user_should_status_401(self):

        #mock_shared_api_client.return_value.login.return_value = False
        #print(mock_shared_api_client.return_value)

        #self.assertEqual(mock_shared_api_client.login(), False)

        data = {"username": "invalid_username", "password": "valid_password"}
        response = self.__make_post_request(data)
        
        self.assertEqual(response.status_code,401)

        response_data = self.__get_response_data(response)
        self.assertIn("message", response_data)
        self.assertEqual(response_data["message"], "invalid")

    def est_invalid_password_should_status_401(self):
        data = {"username": "valid_username", "password": "invalid_password"}
        response = self.__make_post_request(data)
        
        self.assertEqual(response.status_code,401)

        response_data = self.__get_response_data(response)
        self.assertIn("message", response_data)
        self.assertEqual(response_data["message"], "invalid")


if __name__ == "__main__":
    unittest.main()