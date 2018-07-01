#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys
import unittest
from unittest.mock import patch, MagicMock

from flask import json

import app
from mocks.friend_requests_successful_mock import request_detail_success
from models.friend_request import FriendRequestModel


class RequestsTest(unittest.TestCase):

    def setUp(self):
        self.app = app.app.test_client()

    def __make_get_request(self, request_id):
        url = 'api/v1/befriend/requests/' + request_id
        headers = {'Content-type': 'application/json',
                   'Accept': 'text/plain'}
        response = self.app.get(url, headers=headers)
        return response

    @staticmethod
    def __get_response_data(response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))

    @patch('controllers.friend_request_detail_controller.get_user_id')
    @patch('auth_service.is_authenticated')
    def test_get_requests_successful(self, auth_mock, auth_user_mock):
        auth_mock.return_value = True
        auth_user_mock.return_value = 2
        request_id = '6ae66a31d4ef925dac59a95b'

        response = self.__make_get_request(request_id)

        self.assertEqual(200, response.status_code)
        response_data = self.__get_response_data(response)
        self.assertEqual(request_detail_success, response_data)

    @patch('controllers.friend_request_detail_controller.get_user_id')
    @patch('auth_service.is_authenticated')
    def test_get_requests_wrong_user_fail(self, auth_mock, auth_user_mock):
        auth_mock.return_value = True
        auth_user_mock.return_value = 5
        request_id = '6ae66a31d4ef925dac59a95b'

        response = self.__make_get_request(request_id)

        self.assertEqual(409, response.status_code)

    @patch('auth_service.is_authenticated')
    def test_get_requests_no_req_fail(self, auth_mock):
        auth_mock.return_value = True
        request_id = ''

        response = self.__make_get_request(request_id)

        self.assertEqual(404, response.status_code)

    @patch('controllers.friend_request_detail_controller.FriendRequestModel')
    @patch('controllers.friend_request_detail_controller.FriendRequestDetailController._validate_request_participant')
    @patch('auth_service.is_authenticated')
    def test_delete_request_success(self, auth_mock, auth_user_mock, model_mock):
        auth_mock.return_value = True
        auth_user_mock.return_value = True
        request_id = '6ae66a31d4ef925dac59a95b'
        model_mock.get_friend_request.return_value = FriendRequestModel.get_friend_request(request_id)
        model_mock.remove_friend_request.return_value = "deleted"

        response = self.__make_get_request(request_id)

        self.assertEqual(200, response.status_code)
        model_mock.get_friend_request.assert_called_with(request_id)
        # model_mock.remove_friend_request.assert_called_with(request_id)
