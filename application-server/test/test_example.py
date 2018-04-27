import unittest
import requests
import json
import sys
import app
    
class TestFlaskApiUsingRequests(unittest.TestCase):
    def test_hello_world(self):
        response = requests.get('http://0.0.0.0:5858/api/v1')
        self.assertEqual(response.json(), {'hello': 'root'})

if __name__ == "__main__":
    unittest.main()
