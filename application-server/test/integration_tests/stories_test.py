#!/usr/bin/python
# -*- coding: utf-8 -*-

import json
import sys
import unittest
from mocks.storie_successful_mock import stories_request_mock, stories_request_response_mock
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

    def _make_post_request(self, request):
        headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
        return self.app.post('api/v1/stories', data=json.dumps(request), headers=headers)

    @staticmethod
    def __get_response_data(response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))

    def test_successful_get_stories_should_return_status_200(self):
        user_id = '10'
        response = self.__make_get_request(user_id)

        self.assertEqual(200, response.status_code)
        response_data = self.__get_response_data(response)
        self.assertTrue(isinstance(response_data, list))
        self.assertTrue(all(story["story_type"] == "normal" for story in response_data))

    def test_successful_create_storie(self):
        response = self._make_post_request(stories_request_mock)
        self.assertEqual(200, response.status_code)
        data = self.__get_response_data(response)
        self.assertEqual(stories_request_response_mock["description"], data["description"])
        self.assertEqual(stories_request_response_mock["expired_time"], data["expired_time"])
        self.assertEqual(stories_request_response_mock["location"], data["location"])
        self.assertEqual(stories_request_response_mock["multimedia"], data["multimedia"])
        self.assertEqual(stories_request_response_mock["story_type"], data["story_type"])
        self.assertEqual(stories_request_response_mock["title"], data["title"])
        self.assertEqual(stories_request_response_mock["updated_time"], data["updated_time"])
        self.assertEqual(stories_request_response_mock["user_id"], data["user_id"])
        self.assertEqual(stories_request_response_mock["visibility"], data["visibility"])

