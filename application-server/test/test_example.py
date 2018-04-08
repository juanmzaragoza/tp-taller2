import unittest
import requests
import json
import sys

try:
    import app.app as app #for docker test
except ImportError:
    import app #for local test
    

class TestFlaskApiUsingRequests(unittest.TestCase):
    def test_hello_world(self):
        response = requests.get('http://0.0.0.0:5858')
        self.assertEqual(response.json(), {'hello': 'Jose'})


class TestFlaskApi(unittest.TestCase):
    def setUp(self):
        self.app = app.app.test_client()#for local test

    def test_hello_world(self):
        response = self.app.get('/')
        self.assertEqual(json.loads(response.get_data().decode(sys.getdefaultencoding())), {'hello': 'Jose'})

if __name__ == "__main__":
    unittest.main()
