import unittest
import unittest.mock as mock
from models.friend import FriendModel
from controllers.friend_controller import FriendController
from controllers.response_builder import ResponseBuilder
from unit_tests.mocks.friend_successful_mock import friends_successful_mock, friends_raw_mock
from unit_tests.mocks.errors_mock import no_data_found_mock, no_db_conn_mock
from errors_exceptions.no_data_found_exception import NoDataFoundException
from api_client.db_connection_error import DBConnectionError
from controllers.error_handler import ErrorHandler


class TestFriendApi(unittest.TestCase):

    def test_user_friends_db_conn_failed(self):
        user_id = '1'
        FriendModel.get_friends_by_user_id = \
            mock.MagicMock(side_effect=DBConnectionError(""))
        ErrorHandler.create_error_response = \
            mock.MagicMock(return_value=no_db_conn_mock)
        service = FriendController()
        service._create_get_friends_response = \
            mock.MagicMock(return_value=friends_successful_mock)
        self.assertEqual(service.get_friends_by_user_id(user_id), no_db_conn_mock)

    def test_user_friends_successful(self):
        user_id = '1'
        FriendModel.get_friends_by_user_id = \
            mock.MagicMock(return_value=friends_raw_mock)
        service = FriendController()
        service._create_get_friends_response = \
            mock.MagicMock(return_value=friends_successful_mock)
        self.assertEqual(service.get_friends_by_user_id(user_id), friends_successful_mock)
