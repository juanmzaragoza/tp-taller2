import unittest
import unittest.mock as mock
from unittest.mock import patch

from api_client.db_connection_error import DBConnectionError
from controllers.error_handler import ErrorHandler
from errors_exceptions.friendship_already_exists_exception import FriendshipAlreadyExistsException
from controllers.friend_controller import FriendController
from mocks.errors_mock import no_db_conn_mock
from mocks.friend_successful_mock import friends_successful_mock, friends_raw_mock
from models.friend import FriendModel


class TestFriendApi(unittest.TestCase):

    def test_user_friends_db_conn_failed(self):
        user_id = '1'
        FriendModel.get_friends_by_user_id = mock.MagicMock(side_effect=DBConnectionError(''))
        ErrorHandler.create_error_response = mock.MagicMock(return_value=no_db_conn_mock)
        service = FriendController()
        service._create_get_friends_response = mock.MagicMock(return_value=friends_successful_mock)
        self.assertEqual(service.get_friends_by_user_id(user_id),no_db_conn_mock)

    def test_user_friends_successful(self):
        user_id = '1'
        FriendModel.get_friends_by_user_id = mock.MagicMock(return_value=friends_raw_mock)
        service = FriendController()
        service._create_get_friends_response = mock.MagicMock(return_value=friends_successful_mock)
        self.assertEqual(service.get_friends_by_user_id(user_id),friends_successful_mock)

    @patch('models.storie.MongoController')
    def test_create_friend(self, mock_db):
        mock_db_instance = mock.MagicMock()
        mock_db.return_value.get_mongodb_instance.return_value = mock_db_instance

        user_id_sender = '1'
        user_id_rcv = '5'
        result = FriendModel.create_friend({'user_id_sender': user_id_sender,'user_id_rcv': user_id_rcv})
        self.assertEqual(user_id_sender, result['user_id_sender'])
        self.assertEqual(user_id_rcv, result['user_id_rcv'])
   
    @patch('models.storie.MongoController')
    def test_friend_already_exists(self, mock_db):
        mock_db_instance = mock.MagicMock()
        mock_db.return_value.get_mongodb_instance.return_value = mock_db_instance

        user_id_sender = '1'
        user_id_rcv = '10'
        data = {'user_id_sender': user_id_sender,'user_id_rcv': user_id_rcv}
        self.assertRaises(FriendshipAlreadyExistsException,FriendModel.create_friend, data)
