import unittest
import unittest.mock as mock

from api_client.db_connection_error import DBConnectionError
from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.be_friend_detail_controller import BeFriendDetailController
from controllers.db_controller import MongoController
from controllers.error_handler import ErrorHandler
from mocks.errors_mock import no_db_conn_mock
from mocks.friend_requests_successful_mock import friends_requests_successful_mock, friends_requests_raw_mock, \
    friends_requests_creation_successful_mock
from models.friend_request import FriendRequestModel


class TestFriendRequestApi(unittest.TestCase):

    def test_user_friends_rcv_requests_db_conn_failed(self):
        user_id = '1'
        FriendRequestModel.get_friends_requests_rcv_by_user_id = mock.MagicMock(side_effect=DBConnectionError(""))
        ErrorHandler.create_error_response = mock.MagicMock(return_value=no_db_conn_mock)
        service = BeFriendDetailController()
        service._create_get_friends_requests_response = mock.MagicMock(return_value=friends_requests_successful_mock)
        self.assertEqual(service.get_friends_requests_rcv_by_user_id(user_id), no_db_conn_mock)

    def test_user_friends_rcv_requests_successful(self):
        user_id = '1'
        FriendRequestModel.get_friends_requests_rcv_by_user_id = mock.MagicMock(return_value=friends_requests_raw_mock)
        service = BeFriendDetailController()
        service._create_get_friends_requests_response = mock.MagicMock(return_value=friends_requests_successful_mock)
        self.assertEqual(service.get_friends_requests_rcv_by_user_id(user_id), friends_requests_successful_mock)

    def test_user_friends_sent_requests_db_conn_failed(self):
        user_id = '1'
        FriendRequestModel.get_friends_requests_sent_by_user_id = mock.MagicMock(side_effect=DBConnectionError(""))
        ErrorHandler.create_error_response = mock.MagicMock(return_value=no_db_conn_mock)
        service = BeFriendDetailController()
        service._create_get_friends_requests_response = mock.MagicMock(return_value=friends_requests_successful_mock)
        self.assertEqual(service.get_friends_requests_sent_by_user_id(user_id), no_db_conn_mock)

    def test_user_friends_sent_requests_successful(self):
        user_id = '1'
        FriendRequestModel.get_friends_requests_sent_by_user_id = mock.MagicMock(return_value=friends_requests_raw_mock)
        service = BeFriendDetailController()
        service._create_get_friends_requests_response = mock.MagicMock(return_value=friends_requests_successful_mock)
        self.assertEqual(service.get_friends_requests_sent_by_user_id(user_id), friends_requests_successful_mock)

    @mock.patch('models.friend_request.MongoController')
    def test_create_friend_request_successful(self, db_controller_name_mock):
        db_controller_mock = db_controller_name_mock.return_value
        side_effects = [mock.Mock(), MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD),
                        MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD),
                        MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD),
                        MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD),
                        MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)]
        db_controller_mock.get_mongodb_instance = mock.Mock(side_effect=side_effects)
        FriendRequestModel.friend_request_exists = mock.MagicMock(return_value= False)

        request = FriendRequestModel.create_friend_request(1, 5, 'hello', 'picId')

        self.assertEqual(request["user_id_sender"], friends_requests_creation_successful_mock["user_id_sender"])
        self.assertEqual(request["user_id_rcv"], friends_requests_creation_successful_mock["user_id_rcv"])
        self.assertEqual(request["message"], friends_requests_creation_successful_mock["message"])
        self.assertEqual(request["picture"], friends_requests_creation_successful_mock["picture"])
