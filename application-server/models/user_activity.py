from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from controllers.date_controller import DateController

class UserActivityModel():

	@staticmethod
	def update_user_activiy(user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		UserActivityModel._delete_user_activiy(user_id)
		activity = {
			"user_id": user_id,
			"date": DateController.now()
		}
		db.user_activities.insert(activity)

	@staticmethod
	def _delete_user_activiy(user_id):
		db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
		db.user_activities.remove({'user_id': user_id})

	@staticmethod
	def count_today_activities():
		db = MongoController.get_mongodb_instance(MONGODB_USER,MONGODB_PASSWD)
		date_from = DateController.today()
		date_to = DateController.tomorrow()
		count = db.user_activities.find({
			"$and" : [
				{ "date" : {'$gte': date_from} },
				{ "date" : {'$lt': date_to} }
			]
		}).count()
		return count
