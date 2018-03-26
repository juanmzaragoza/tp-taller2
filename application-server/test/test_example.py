import unittest
import requests
import json
import sys
import app #for local test
#import app.app#for docker test

class TestFlaskApiUsingRequests(unittest.TestCase):
    def test_hello_world(self):
        response = requests.get('http://0.0.0.0:5858')
        self.assertEqual(response.json(), {'hello': 'world'})


class TestFlaskApi(unittest.TestCase):
    def setUp(self):
        self.app = app.app.test_client()
        #self.app = app.app.app.test_client()#for docker test

    def test_hello_world(self):
        response = self.app.get('/')
        self.assertEqual(json.loads(response.get_data().decode(sys.getdefaultencoding())), {'hello': 'world'})


if __name__ == "__main__":
    unittest.main()
