import unittest
import unittest.mock as mock
from models.friend_request import FriendRequestModel
from controllers.friend_request_controller import FriendRequestController
from controllers.response_builder import ResponseBuilder
from unit_tests.mocks.friend_requests_successful_mock import friends_requests_successful_mock, friends_requests_raw_mock
from unit_tests.mocks.errors_mock import no_data_found_mock, no_db_conn_mock
from errors_exceptions.no_data_found_exception import NoDataFoundException
from api_client.db_connection_error import DBConnectionError
from controllers.error_handler import ErrorHandler


class TestFriendRequestApi(unittest.TestCase):

    def test_user_friends_rcv_requests_db_conn_failed(self):
        user_id = '1'
        FriendRequestModel.get_friends_requests_rcv_by_user_id = \
            mock.MagicMock(side_effect=DBConnectionError(""))
        ErrorHandler.create_error_response = \
            mock.MagicMock(return_value=no_db_conn_mock)
        service = FriendRequestController()
        service._create_get_friends_requests_response = \
            mock.MagicMock(return_value=friends_requests_successful_mock)
        self.assertEqual(service.get_friends_requests_rcv_by_user_id(user_id), no_db_conn_mock)

    def test_user_friends_rcv_requests_successful(self):
        user_id = '1'
        FriendRequestModel.get_friends_requests_rcv_by_user_id = \
            mock.MagicMock(return_value=friends_requests_raw_mock)
        service = FriendRequestController()
        service._create_get_friends_requests_response = \
            mock.MagicMock(return_value=friends_requests_successful_mock)
        self.assertEqual(service.get_friends_requests_rcv_by_user_id(user_id), friends_requests_successful_mock)

    def test_user_friends_sent_requests_db_conn_failed(self):
        user_id = '1'
        FriendRequestModel.get_friends_requests_sent_by_user_id = \
            mock.MagicMock(side_effect=DBConnectionError(""))
        ErrorHandler.create_error_response = \
            mock.MagicMock(return_value=no_db_conn_mock)
        service = FriendRequestController()
        service._create_get_friends_requests_response = \
            mock.MagicMock(return_value=friends_requests_successful_mock)
        self.assertEqual(service.get_friends_requests_sent_by_user_id(user_id), no_db_conn_mock)

    def test_user_friends_sent_requests_successful(self):
        user_id = '1'
        FriendRequestModel.get_friends_requests_sent_by_user_id = \
            mock.MagicMock(return_value=friends_requests_raw_mock)
        service = FriendRequestController()
        service._create_get_friends_requests_response = \
            mock.MagicMock(return_value=friends_requests_successful_mock)
        self.assertEqual(service.get_friends_requests_sent_by_user_id(user_id), friends_requests_successful_mock)
