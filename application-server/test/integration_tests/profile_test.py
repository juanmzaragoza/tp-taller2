#!/usr/bin/python
# -*- coding: utf-8 -*-

import unittest
import unittest.mock as mock
from unittest.mock import patch
from models.user_data import UserDataModel
from controllers.storie_controller import StorieController
from controllers.friend_controller import FriendController
from controllers.friend_request_controller import FriendRequestController
from controllers.profile_controller import ProfileController
from controllers.response_builder import ResponseBuilder
from mocks.profile_successful_mock import *
import requests
import json
import sys
import app


class TestFlaskApi(unittest.TestCase):

    def setUp(self):
        self.app = app.app.test_client()

    def __make_get_request(self, userId):
        url = 'api/v1/profiles/' + userId
        headers = {'Content-type': 'application/json',
                   'Accept': 'text/plain'}
        response = self.app.get(url, headers=headers)
        return response

    def __get_response_data(self, response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))

    def test_successful_profile_data_should_return_status_200(self):

        # execution

        userId = '5ae66a31d4ef925dac59a94b'
        response = self.__make_get_request(userId)

        # assertions

        self.assertEqual(response.status_code, 200)
        response_data = self.__get_response_data(response)
        self.assertIn('name', response_data['profile'])
        self.assertEqual('Pepe', response_data['profile']['name'])
        self.assertIn('last_name', response_data['profile'])
        self.assertEqual('Gomez', response_data['profile']['last_name'])
        self.assertIn('email', response_data['profile'])
        self.assertEqual('pepe@email.com', response_data['profile'
                         ]['email'])
        self.assertIn('picture', response_data['profile'])
        self.assertIn('stories', response_data['profile'])

    def _test_profile_no_data_found_shouldreturn_status_404(self):
        userId = '1'
        response = self.__make_get_request(userId)
        self.assertEqual(response.status_code, 404)
        response_data = self.__get_response_data(response)
        self.assertIn('code', response_data)
        self.assertEqual(404, response_data['code'])
        self.assertIn('message', response_data)
