import json
import sys
import unittest

import app


class TestStatsApi(unittest.TestCase):

    def setUp(self):
        self.app = app.app.test_client()

    def __make_get_request(self, data):
        url = "api/v1/stats"
        headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
        response = self.app.get(url, data=json.dumps(data), headers=headers)
        return response

    def __get_response_data(self, response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))


    def test_successful_stats_should_return_status_200(self):
        # set up
        data = {}
        # execution
        response = self.__make_get_request(data)
        #assertions
        self.assertEqual(response.status_code,200)
        response_data = self.__get_response_data(response)
        self.assertIn("numAcceptedContactsToday", response_data["stats"])
        self.assertIn("numFastStoriesToday", response_data["stats"])
        self.assertIn("numStories", response_data["stats"])
        self.assertIn("numStoriesToday", response_data["stats"])
        self.assertIn("numUsers", response_data["stats"])
        self.assertIn("numUsersActiveToday", response_data["stats"])
        self.assertIn("numUsersMessages", response_data["stats"])
        self.assertIn("numUsersMessagesToday", response_data["stats"])
