import pymongo
import time
import pytz
import calendar
from datetime import datetime, timedelta

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
		#date_time = datetime.datetime.now(pytz.timezone('America/Argentina/Buenos_Aires'))
		date_time = datetime.utcnow()
		return date_time
	
	@staticmethod
	def get_date_time_with_format(date):
		date_time = ""
		if (date != ""):
			##date_time = date.strftime('%d/%m/%Y %H:%M:%S')
			date_time = DateController.utc_to_bsas(date)
		return date_time
	
	@staticmethod
	def utc_to_bsas(utc_datetime):
		bsas_tz = pytz.timezone('America/Argentina/Buenos_Aires')
		bsas_datetime = utc_datetime.replace(tzinfo=pytz.utc).astimezone(bsas_tz)
		return bsas_tz.normalize(bsas_datetime).strftime("%d/%m/%Y %H:%M:%S")

	@staticmethod
	def get_date_time_inc_by_hours(hours):
		date = DateController.get_date_time() + datetime.timedelta(hours=hours)
		return date
	
	@staticmethod
	def get_date_time_dec_by_days(days):
		date = DateController.get_date_time() - datetime.timedelta(days=days)
		return date
		
	@staticmethod
	def today():
		now = datetime.utcnow()
		today = datetime(now.year, now.month, now.day)
		return today

	@staticmethod
	def tomorrow():
		today = DateController.today()
		tomorrow = today + datetime.timedelta(1)
		return tomorrow

	@staticmethod
	def now():
		now = datetime.utcnow()
		return now
	
	@staticmethod
	def get_past_days(date):
		date = datetime.strptime(date, "%d/%m/%Y %H:%M:%S")
		now = DateController.get_date_time()
		diff = now - date
		return diff.days
	