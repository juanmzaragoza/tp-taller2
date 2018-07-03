import json
import sys
import unittest
from unittest.mock import patch
from flask import request
import app


class TestCommentsApi(unittest.TestCase):

    def setUp(self):
        self.app = app.app.test_client()

    def __make_post_request(self, data):
        url = 'api/v1/stories/reactions'
        headers = {'Content-type': 'application/json',
                   'Accept': 'text/plain'}
        response = self.app.post(url, data=json.dumps(data),
                                 headers=headers)
        return response

    def __make_delete_request(self, reaction_id):
        url = 'api/v1/stories/reactions/' + reaction_id
        headers = {'Content-type': 'application/json',
                   'Accept': 'text/plain'}
        response = self.app.delete(url, headers=headers)
        return response

    @staticmethod
    def __get_response_data(response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))

    @patch('auth_service.get_user_id')
    @patch('auth_service.is_authenticated')
    def test_successful_reaction_lifetime_should_return_status_200(self,
            mock_is_authenticated, mock_get_user_id):
        mock_is_authenticated.return_value = True
        mock_get_user_id.return_value = 10
        data = {
            'storie_id': '5ae66a31d4ef925dac69a95b',
            'user_id': '10',
            '_rev': '',
            'date': '',
            '_id': '',
            'reaction': 'LIKE',
            }

        res = self.__make_post_request(data)
        response = self.__get_response_data(res)

        self.assertTrue(response['_id'] != '')
        self.assertTrue(response['date'] != '')
        self.assertEqual(response['reaction'], 'LIKE')
        self.assertEqual(response['storie_id'],'5ae66a31d4ef925dac69a95b')
        self.assertEqual(response['_rev'], '')

        reaction_id = response['_id']

        res = self.__make_delete_request(reaction_id)
        response = self.__get_response_data(res)
        self.assertEqual(response['_id'], reaction_id)
