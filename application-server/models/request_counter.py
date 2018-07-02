from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.date_controller import DateController
from controllers.db_controller import MongoController, RETURN_DOCUMENT_AFTER


class RequestCounterModel():

	@staticmethod
	def create_structure():
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		date = DateController.get_date()
		request = db.server_requests.find_one({"date": date})

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
			data['_id'] = date + ' ' + str(hour)
			data['hour'] = hour
			db.server_requests.insert(data)

	@staticmethod
	def get_requests(fromHour = None, toHour = None):
		server_requests = RequestCounterModel._find_requests(fromHour, toHour)

		data = {}
		for reg in server_requests:
			data[reg["hour"]] = {
				"date": reg["date"],
				"hour": reg["hour"],
				"count": reg["count"]
			}
		return list(data.values())

	@staticmethod
	def _find_requests(fromHour, toHour):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)

		date = DateController.get_date()
		andArgs = [{"date": date}]

		if (fromHour is not None):
			andArgs.append({"hour": {'$gte': int(fromHour)}})

		if (toHour is not None):
			andArgs.append({"hour": {'$lte': int(toHour)}})

		server_requests = db.server_requests.find({"$and": andArgs})
		return server_requests



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
			request = db.server_requests.find_one(data)

		response = db.server_requests.find_one_and_update(
			{'date': date, 'hour': hour},
			{'$inc': {'count': 1}},
			return_document=RETURN_DOCUMENT_AFTER
		)

		#response["date"] = DateController.get_date_time_with_format(response["date"])
		return response
