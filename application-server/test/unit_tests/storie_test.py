import unittest
import unittest.mock as mock

from mock import patch

from api_client.db_connection_error import DBConnectionError
from controllers.error_handler import ErrorHandler
from controllers.storie_detail_controller import StorieDetailController
from mocks.errors_mock import no_db_conn_mock
from mocks.storie_successful_mock import stories_successful_mock, stories_raw_mock, stories_request_response_mock, \
    stories_request_mock
from models.storie import StorieModel


class TestStorieApi(unittest.TestCase):

    def test_user_stories_db_conn_failed(self):
        user_id = '1'
        StorieModel.get_profile_stories_by_user_id = mock.MagicMock(side_effect=DBConnectionError(""))
        ErrorHandler.create_error_response = mock.MagicMock(return_value=no_db_conn_mock)
        service = StorieDetailController()
        service._create_get_stories_response = mock.MagicMock(return_value=stories_successful_mock)
        self.assertEqual(service.get_stories_by_user_id(user_id), no_db_conn_mock)

    def test_user_stories_successful(self):
        user_id = '1'
        StorieModel.get_profile_stories_by_user_id = mock.MagicMock(return_value=stories_raw_mock)
        service = StorieDetailController()
        service._create_get_stories_response = mock.MagicMock(return_value=stories_successful_mock)
        self.assertEqual(service.get_stories_by_user_id(user_id), stories_successful_mock)

    @patch('models.storie.MongoController')
    def test_successful_create_story(self, mock_db):
        mock_db_instance = mock.MagicMock()
        mock_db.return_value.get_mongodb_instance.return_value = mock_db_instance

        data = StorieModel.create_user_storie(stories_request_mock)
        self.assertEqual(stories_request_response_mock["description"], data["description"])
        self.assertEqual(stories_request_response_mock["expired_time"], data["expired_time"])
        self.assertEqual(stories_request_response_mock["location"], data["location"])
        self.assertEqual(stories_request_response_mock["multimedia"], data["multimedia"])
        self.assertEqual(stories_request_response_mock["story_type"], data["story_type"])
        self.assertEqual(stories_request_response_mock["title"], data["title"])
        self.assertEqual(stories_request_response_mock["updated_time"], data["updated_time"])
        self.assertEqual(stories_request_response_mock["user_id"], data["user_id"])
        self.assertEqual(stories_request_response_mock["visibility"], data["visibility"])
