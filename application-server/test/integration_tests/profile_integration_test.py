#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys
import unittest
from unittest.mock import patch

from flask import json

import app
from mocks.profile_successful_mock import profile1


class ProfileTest(unittest.TestCase):

    def setUp(self):
        self.app = app.app.test_client()

    def __make_get_request(self, user_id):
        url = 'api/v1/profiles/' + user_id
        headers = {'Content-type': 'application/json',
                   'Accept': 'text/plain'}
        response = self.app.get(url, headers=headers)
        return response

    @staticmethod
    def __get_response_data(response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))

    @patch('auth_service.is_authenticated')
    def test_get_profile_successful(self, auth_mock):
        auth_mock.return_value = True
        user_id = '1'

        response = self.__make_get_request(user_id)

        self.assertEqual(200, response.status_code)
        response_data = self.__get_response_data(response)
        self.assertEqual(profile1, response_data)

