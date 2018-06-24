import flask_restful
import json
import requests

class RuleMachineProxy():
	
	def __init__(self):
		self.headers = {
			'Content-type': 'application/json', 
			'Accept': 'application/json'
		}
	
	def new_rule_process(self):
		data = {'subject':'renew'}
		requests.post('http://localhost:5000/importance/events', data=json.dumps(data), headers=self.headers)
	
	def process_storie_data(self, storie_data):
		storie_data["subject"] = "comments"
		requests.post('http://localhost:5000/importance/events', data=json.dumps(storie_data), headers=self.headers)
		storie_data["subject"] = "reactions"
		requests.post('http://localhost:5000/importance/events', data=json.dumps(storie_data), headers=self.headers)
		storie_data["subject"] = "date"
		requests.post('http://localhost:5000/importance/events', data=json.dumps(storie_data), headers=self.headers)
		#storie_data["subject"] = "friends"
		#requests.post('http://localhost:5000/importance/events', data=json.dumps(storie_data), headers=self.headers)
		#storie_data["subject"] = "stories"
		#requests.post('http://localhost:5000/importance/events', data=json.dumps(storie_data), headers=self.headers)
				
	def get_results(self):
		r = requests.get('http://localhost:5000/importance/state')
		return json.loads(r.text)
