from datetime import datetime, timedelta

import pytz


class DateController():

	@staticmethod
	def get_date():
		date_time = DateController.utc_to_bsas(datetime.utcnow())
		date = date_time.strftime('%d/%m/%Y')
		return date

	@staticmethod
	def get_hour():
		date_time = DateController.utc_to_bsas(datetime.utcnow())
		hour = date_time.strftime('%H')
		return hour

	@staticmethod
	def get_date_time():
		##date_time = datetime.datetime.now(pytz.timezone('America/Argentina/Buenos_Aires'))
		date_time = datetime.utcnow()
		#date_time = DateController.utc_to_bsas(datetime.utcnow())
		return date_time

	@staticmethod
	def get_date_time_with_format(date):
		date_time = ""
		if (date != ""):
			#date_time = date.strftime('%d/%m/%Y %H:%M:%S')
			date_time = DateController.utc_to_bsas(date).strftime("%d/%m/%Y %H:%M:%S")
		return date_time

	@staticmethod
	def utc_to_bsas(utc_datetime):
		bsas_tz = pytz.timezone('America/Argentina/Buenos_Aires')
		bsas_datetime = utc_datetime.replace(tzinfo=pytz.utc).astimezone(bsas_tz)
		return bsas_tz.normalize(bsas_datetime)

	@staticmethod
	def get_date_time_inc_by_hours(hours):
		date = DateController.get_date_time() + timedelta(hours=hours)
		return date

	@staticmethod
	def get_date_time_dec_by_days(days):
		date = DateController.get_date_time() - timedelta(days=days)
		return date

	@staticmethod
	def today():
		now = DateController.utc_to_bsas(datetime.utcnow())
		today = datetime(now.year, now.month, now.day)
		return today

	@staticmethod
	def tomorrow():
		today = DateController.today()
		tomorrow = today + timedelta(1)
		return tomorrow

	@staticmethod
	def now():
		now = DateController.utc_to_bsas(datetime.utcnow())
		return now

	@staticmethod
	def get_past_days(date):
		date = datetime.strptime(date, "%d/%m/%Y %H:%M:%S")
		now = DateController.get_date_time()
		diff = now - date
		return diff.days
