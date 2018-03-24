import unittest
import example
import requests
import json
import sys

class TestFlaskApiUsingRequests(unittest.TestCase):
    def test_hello_world(self):
        response = requests.get('http://0.0.0.0:5858')
        self.assertEqual(response.json(), {'hello': 'world'})


class TestFlaskApi(unittest.TestCase):
    def setUp(self):
        self.app = example.app.test_client()

    def test_hello_world(self):
        response = self.app.get('/')
        self.assertEqual(json.loads(response.get_data().decode(sys.getdefaultencoding())), {'hello': 'world'})


if __name__ == "__main__":
    unittest.main()
