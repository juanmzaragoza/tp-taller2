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

    def test_e(self):
        url = "/token"
        data = {"username": "aaaaa", "password": "beeeee"}
        headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
        response = self.app.post(url, data=json.dumps(data), headers=headers)
        self.assertEqual(response.status_code,200)
        self.assertEqual(
            json.loads(response.get_data().decode(sys.getdefaultencoding())),
            {
                "password":  "beeeee"
            }
        )


    def est_missing_username_should_status_400(self):
        url = "/token"
        data = {"password": "beeeee"}
        headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
        response = self.app.post(url, data=json.dumps(data), headers=headers)
        self.assertEqual(response.status_code,400)

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
