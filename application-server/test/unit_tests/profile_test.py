#!/usr/bin/python
# -*- coding: utf-8 -*-

import unittest
import unittest.mock as mock
from models.user_data import UserDataModel
from controllers.storie_detail_controller import StorieDetailController
from controllers.friend_controller import FriendController
from controllers.be_friend_detail_controller import BeFriendDetailController
from controllers.profile_controller import ProfileController
from controllers.response_builder import ResponseBuilder
from mocks.profile_successful_mock import *
from mocks.errors_mock import no_data_found_mock, no_db_conn_mock
from errors_exceptions.no_data_found_exception import NoDataFoundException
from api_client.db_connection_error import DBConnectionError
from controllers.error_handler import ErrorHandler


class TestProfileApi(unittest.TestCase):

    def test_user_profile_db_conn_failed(self):
        user_id = '1'
        UserDataModel.get_user_data_by_user_id = \
            mock.MagicMock(side_effect=DBConnectionError(""))
        ErrorHandler.create_error_response = \
            mock.MagicMock(return_value=no_db_conn_mock)
        service = ProfileController()
        ResponseBuilder.get_build_response = \
            mock.MagicMock(return_value=profile_successful_mock)
        self.assertEqual(service.get(user_id), no_db_conn_mock)

    def test_user_profile_not_exists(self):
        user_id = '1'
        UserDataModel.get_user_data_by_user_id = \
            mock.MagicMock(side_effect=NoDataFoundException)
        ErrorHandler.create_error_response = \
            mock.MagicMock(return_value=no_data_found_mock)
        service = ProfileController()
        ResponseBuilder.get_build_response = \
            mock.MagicMock(return_value=profile_successful_mock)
        self.assertEqual(service.get(user_id), no_data_found_mock)

    def test_user_profile_successful(self):
        user_id = '1'
        UserDataModel.get_user_data_by_user_id = \
            mock.MagicMock(return_value=user_data_successful_mock)
        storie = StorieDetailController()
        storie.get_stories_by_user_id = \
            mock.MagicMock(return_value=stories_successful_mock)
        friend = FriendController()
        friend.get_friends_by_user_id = \
            mock.MagicMock(return_value=friends_successful_mock)
        friend_request = BeFriendDetailController()
        friend_request.get_friends_requests_rcv_by_user_id = \
            mock.MagicMock(return_value=rcv_requests_successful_mock)
        friend_request.get_friends_requests_sent_by_user_id = \
            mock.MagicMock(return_value=sent_requests_successful_mock)
        service = ProfileController()
        ResponseBuilder.get_build_response = \
            mock.MagicMock(return_value=profile_successful_mock)
        self.assertEqual(service.get(user_id), profile_successful_mock)
