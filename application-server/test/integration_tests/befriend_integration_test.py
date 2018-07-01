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

    def __make_post_request(self, request):
        url = 'api/v1/befriend'
        headers = {'Content-type': 'application/json',
                   'Accept': 'text/plain'}
        response = self.app.post(url, headers=headers, data=json.dumps(request))
        return response

    @staticmethod
    def __get_response_data(response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))

    @patch('controllers.be_friend_controller.validate_sender')
    @patch('controllers.be_friend_controller.FriendRequestModel')
    @patch('auth_service.is_authenticated')
    def test_befriend_successful(self, auth_mock, model_mock, validation_mock):
        auth_mock.return_value = True
        model_mock.create_friend_request.return_value = 'Success'
        user_sender_id = '1'
        user_rcv_id = '2'
        msg = 'test'
        picture = '185743985798'
        request = {
            'user_id': user_sender_id,
            'rcv_user_id': user_rcv_id,
            'message': msg,
            'picture': picture
        }

        response = self.__make_post_request(request)

        self.assertEqual(200, response.status_code)
        model_mock.create_friend_request.assert_called_with(user_sender_id, user_rcv_id, msg, picture)
