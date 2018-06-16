from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.date_controller import DateController
from errors_exceptions.no_data_found_exception import NoDataFoundException
from errors_exceptions.data_already_exists_exception import DataAlreadyExistsException

class RequestCounterModel():
	
	@staticmethod
	def create_structure():
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		request = db.server_requests.find_one()
		
		if (request != None):
			return
			
		date = DateController.get_date()
		hour = 0
		data = {
					'date': date,
					'_id': 0,
					'hour': 0,
					'count': 0
				}
				
		for hour in range(0, 24):
			data['_id'] = hour		
			data['hour'] = hour		
			db.server_requests.insert(data)
				
	@staticmethod
	def get_requests():
		friends_requests_user_id = []
		response = []
		data = {}
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		
		RequestCounterModel.create_structure()
		server_requests = db.server_requests.find()
		for reg in server_requests:
			data[reg["hour"]] = {
									"date": reg["date"],
									"hour": reg["hour"],
									"count": reg["count"]
								}
		
		return list(data.values())
		
	@staticmethod
	def inc_requests():
		date = DateController.get_date()
		hour = int(DateController.get_hour()) 
		
		response = {}
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		data = {'date': date, 'hour': hour}
		request = db.server_requests.find_one(data)
		
		if (request == None):
			RequestCounterModel.create_structure()
			data['count'] = 1
			response = db.server_requests.find_and_modify({'hour': hour},{'$set': data})
		else:
			num_requests = request['count'] + 1
			data['count'] = num_requests
			response = db.server_requests.find_and_modify({'date': date, 'hour': hour},{'$set': data})

		return response
