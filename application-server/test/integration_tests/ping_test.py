import json
import sys
import unittest

import app


class TestPingApi(unittest.TestCase):

    def setUp(self):
        self.app = app.app.test_client()

    def __make_get_request(self, data):
        url = "api/v1/ping"
        headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
        response = self.app.get(url, data=json.dumps(data), headers=headers)
        return response

    def __get_response_data(self, response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))


    def test_successful_ping_should_return_status_200(self):
        # set up
        data = {}
        # execution
        response = self.__make_get_request(data)
        #assertions
        self.assertEqual(response.status_code,200)
        response_data = self.__get_response_data(response)
        self.assertIn("status", response_data["server"])
        self.assertEqual("active",response_data["server"]["status"])
