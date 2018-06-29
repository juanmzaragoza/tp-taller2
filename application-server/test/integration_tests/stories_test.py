#!/usr/bin/python
# -*- coding: utf-8 -*-

import json
import sys
import unittest
from unittest.mock import patch

import app


class TestStoriesApi(unittest.TestCase):

    def setUp(self):
        self.app = app.app.test_client()

    def __make_get_request(self, user_id):
        url = 'api/v1/stories/' + user_id
        headers = {'Content-type': 'application/json',
                   'Accept': 'text/plain'}
        response = self.app.get(url, headers=headers)
        return response

    @staticmethod
    def __get_response_data(response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))

    @patch('auth_service.is_authenticated')
    def test_successful_get_stories_should_return_status_200(self, auth_mock):
        auth_mock.return_value = True
        user_id = '10'
        response = self.__make_get_request(user_id)

        self.assertEqual(200, response.status_code)
        response_data = self.__get_response_data(response)
        self.assertTrue(isinstance(response_data, list))
        self.assertTrue(all(story["story_type"] == "normal" for story in response_data))
