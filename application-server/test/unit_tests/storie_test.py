import unittest
import unittest.mock as mock
from models.storie import StorieModel
from controllers.storie_controller import StorieController
from controllers.response_builder import ResponseBuilder
from unit_tests.mocks.storie_successful_mock import stories_successful_mock, stories_raw_mock
from unit_tests.mocks.errors_mock import no_data_found_mock, no_db_conn_mock
from errors_exceptions.no_data_found_exception import NoDataFoundException
from api_client.db_connection_error import DBConnectionError
from controllers.error_handler import ErrorHandler


class TestStorieApi(unittest.TestCase):

    def test_user_stories_db_conn_failed(self):
        user_id = '1'
        StorieModel.get_stories_by_user_id = \
            mock.MagicMock(side_effect=DBConnectionError(""))
        ErrorHandler.create_error_response = \
            mock.MagicMock(return_value=no_db_conn_mock)
        service = StorieController()
        service._create_get_stories_response = \
            mock.MagicMock(return_value=stories_successful_mock)
        self.assertEqual(service.get_stories_by_user_id(user_id), no_db_conn_mock)

    def test_user_stories_successful(self):
        user_id = '1'
        StorieModel.get_stories_by_user_id = \
            mock.MagicMock(return_value=stories_raw_mock)
        service = StorieController()
        service._create_get_stories_response = \
            mock.MagicMock(return_value=stories_successful_mock)
        self.assertEqual(service.get_stories_by_user_id(user_id), stories_successful_mock)
