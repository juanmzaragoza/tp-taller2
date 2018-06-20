import pymongo
import datetime
import time

class DateController():
	
	@staticmethod
	def get_date():
		date = time.strftime('%d/%m/%Y', time.localtime())
		return date
	
	@staticmethod
	def get_hour():
		hour = time.strftime('%H', time.localtime())
		return hour
				
	@staticmethod
	def get_date_time():
		date_time = datetime.datetime.now()
		#date_time = time.strftime('%d/%m/%Y %H:%M:%S', time.localtime())
		return date_time
		
	@staticmethod
	def get_date_time_with_format(date):
		date_time = ""
		if (date != ""):
			date_time = date.strftime('%d/%m/%Y %H:%M:%S')
		return date_time
	
	@staticmethod
	def get_date_time_inc_by_hours(hours):
		date = DateController.get_date_time() + datetime.timedelta(hours=hours)
		return date

	@staticmethod
	def today():
		now = datetime.datetime.now()
		today = datetime.datetime(now.year, now.month, now.day)
		return today

	@staticmethod
	def tomorrow():
		today = DateController.today()
		tomorrow = today + datetime.timedelta(1)
		return tomorrow
