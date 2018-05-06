from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from errors_exceptions.no_data_found_exception import NoDataFoundException
from bson.objectid import ObjectId

class StorieModel():
      
    @staticmethod
    def get_stories_by_user_id(user_id):
        stories_id = []
        response = []
        db = MongoController.get_mongodb_instance(MONGODB_USER, MONGODB_PASSWD)
        stories_regs = db.users_stories.find({'user_id': user_id})
        for doc in stories_regs:
            stories_id.append(ObjectId(doc["storie_id"]))
            
        stories = db.stories.find({'_id':{"$in":stories_id}});
        for doc in stories:
            response.append(doc)
        
        return response
