import json
import sys
import unittest
from unittest.mock import patch
from flask import request
import app


class TestCommentsApi(unittest.TestCase):

    def setUp(self):
        self.app = app.app.test_client()

    def __make_get_request(self, user_id):
        url = 'api/v1/stories/' + user_id + '/comments'
        headers = {'Content-type': 'application/json',
                   'Accept': 'text/plain'}
        response = self.app.get(url, headers=headers)
        return response

    def __make_post_request(self, data):
        url = 'api/v1/stories/comments'
        headers = {'Content-type': 'application/json',
                   'Accept': 'text/plain'}
        response = self.app.post(url, data=json.dumps(data),
                                 headers=headers)
        return response

    def __make_put_request(self, comment_id, data):
        url = 'api/v1/stories/comments/' + comment_id
        headers = {'Content-type': 'application/json',
                   'Accept': 'text/plain'}
        response = self.app.put(url, data=json.dumps(data),
                                headers=headers)
        return response

    def __make_delete_request(self, comment_id):
        url = 'api/v1/stories/comments/' + comment_id
        headers = {'Content-type': 'application/json',
                   'Accept': 'text/plain'}
        response = self.app.delete(url, headers=headers)
        return response

    @staticmethod
    def __get_response_data(response):
        return json.loads(response.get_data().decode(sys.getdefaultencoding()))

    @patch('auth_service.is_authenticated')
    def test_successful_get_stories_comment_should_return_status_200(self,
            auth_mock):
        auth_mock.return_value = True
        user_id = '10'
        response = self.__make_get_request(user_id)

        self.assertEqual(200, response.status_code)
        response_data = self.__get_response_data(response)
        self.assertTrue(isinstance(response_data, list))
        for comment in response_data:
            print (comment)
        self.assertTrue(all(comment['story_type'] == 'normal'
                        for comment in response_data))

    @patch('auth_service.get_user_id')
    @patch('auth_service.is_authenticated')
    def test_successful_comment_lifetime_should_return_status_200(self,
            mock_is_authenticated, mock_get_user_id):
        mock_is_authenticated.return_value = True
        mock_get_user_id.return_value = 10
        data = {
            'storie_id': '5ae66a31d4ef925dac59a95b',
            'user_id': '10',
            '_rev': '',
            'date': '',
            '_id': '',
            'message': 'New Comment',
            }

        res = self.__make_post_request(data)
        response = self.__get_response_data(res)

        self.assertTrue(response['_id'] != '')
        self.assertTrue(response['date'] != '')
        self.assertEqual(response['message'], 'New Comment')
        self.assertEqual(response['storie_id'], '5ae66a31d4ef925dac59a95b')
        self.assertEqual(response['_rev'], '')

        comment_id = response['_id']
        data['_id'] = comment_id
        data['message'] = 'Updated Comment'

        res = self.__make_put_request(comment_id, data)
        response = self.__get_response_data(res)
        self.assertTrue(response['_rev'] != '')
        self.assertEqual(response['message'], 'Updated Comment')
        self.assertEqual(response['storie_id'], '5ae66a31d4ef925dac59a95b')

        res = self.__make_delete_request(comment_id)
        response = self.__get_response_data(res)
        self.assertEqual(response['_id'], comment_id)
