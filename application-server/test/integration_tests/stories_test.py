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

    def __make_put_request(self, story_id, request):
        url = 'api/v1/stories/' + story_id
        headers = {'Content-type': 'application/json',
                   'Accept': 'text/plain'}
        response = self.app.put(url, headers=headers, data=json.dumps(request))
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

    @patch('controllers.storie_detail_controller.UserActivityModel.log_storie_activity')
    @patch('controllers.storie_detail_controller.StorieModel')
    @patch('controllers.storie_detail_controller.validate_sender')
    @patch('auth_service.is_authenticated')
    def test_successful_put_stories_should_return_status_200(self, auth_mock, validate_mock, model_mock, log_mock):
        auth_mock.return_value = True
        model_mock.get_stories.return_value = {'user_id': 'mock'}
        model_mock.update_storie.return_value = {'user_id': 'mock', '_id': 'mock'}
        user_id = '10'
        storie_id = '1'
        request = {
            'user_id': user_id,
            '_id': storie_id,
            '_rev': '',
            'title': '',
            'location': '',
            'visibility': '',
            'multimedia': '',
            'story_type': 'normal'
        }

        response = self.__make_put_request(storie_id, request)

        self.assertEqual(200, response.status_code)
        model_mock.update_storie.assert_called_with(storie_id, request)
