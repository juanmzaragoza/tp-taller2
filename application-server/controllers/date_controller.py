import pymongo
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
		date_time = time.strftime('%d/%m/%Y %H:%M:%S', time.localtime())
		return date_time
