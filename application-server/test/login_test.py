import unittest
import requests
import json
import sys
import app #for local test
#import app.app#for docker test


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

    def test_e(self):
        url = "/token"
        data = {"username": "aaaaa", "password": "beeeee"}
        headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
        response = self.app.post(url, data=json.dumps(data), headers=headers)
        self.assertEqual(response.status_code,200)

        response_data = self.__get_response_data(response)
        self.assertEqual(
            response_data,
            {
                "password":  "beeeee"
            }
        )


    def test_missing_username_should_status_400(self):
        data = {"password": "beeeee"}
        response = self.__make_post_request(data)

        self.assertEqual(response.status_code,400)

        response_data = self.__get_response_data(response)
        self.assertIn("message", response_data)
        
        self.assertEqual(
            response_data["message"],
            { "username": "username cannot be blank!" }
        )

    def test_missing_password_should_status_400(self):
        data = {"username": "bob"}
        response = self.__make_post_request(data)
        
        self.assertEqual(response.status_code,400)

        response_data = self.__get_response_data(response)
        self.assertIn("message", response_data)
        
        self.assertEqual(
            response_data["message"],
            { "password": "password cannot be blank!" }
        )

    def est_token(self):
        response = self.app.post('/token', json={"username": "aaaaa", "password": "beeeee"})
        self.assertEqual(response.status_code,200)
        self.assertEqual(json.loads(response.get_data().decode(sys.getdefaultencoding())), {
            "metadata": {
                "version": "string"
            },
            "token": {
                "expiresAt": 0,
                "token": "string"
            }
        })


if __name__ == "__main__":
    unittest.main()
