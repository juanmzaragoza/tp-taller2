#!/usr/bin/python
# -*- coding: utf-8 -*-
from constants import MONGODB_USER, MONGODB_PASSWD
from controllers.db_controller import MongoController
from errors_exceptions.no_data_found_exception import NoDataFoundException
from bson.objectid import ObjectId
import time


class StorieModel:

    @staticmethod
    def create_user_storie(body):
        db = MongoController.get_mongodb_instance(MONGODB_USER,
                MONGODB_PASSWD)
        storie_date = time.strftime('%d/%m/%Y hh:mm', time.localtime())
        storie = {
            'created_time': storie_date,
            'updated_time': 0,
            '_rev': body['_rev'],
            'title': body['title'],
            'description': body['description'],
            'location': body['location'],
            'visibility': body['visibility'],
            'multimedia': body['multimedia'],
            'story_type': body['storyType'],
            }
        storie_id = db.stories.insert(storie)
        db.users_stories.insert({'user_id': body['userId'],
                                'storie_id': storie_id})
        response = db.stories.find_one({'_id': ObjectId(storie_id)})
        response['_id'] = str(response['_id'])
        response['user_id'] = body['userId']
        return response

    @staticmethod
    def get_stories():
        stories_id = []
        response = {}
        db = MongoController.get_mongodb_instance(MONGODB_USER,
                MONGODB_PASSWD)
        stories_regs = db.users_stories.find({})
        for doc in stories_regs:
            storie_id = str(doc['storie_id'])
            response[storie_id] = {'user_id': doc['user_id']}
            stories_id.append(ObjectId(storie_id))

        stories = db.stories.find({'_id': {'$in': stories_id}})
        for doc in stories:
            storie_id = str(doc['_id'])
            response[storie_id].update({'_id': storie_id})
            response[storie_id].update({'_rev': doc['_rev']})
            response[storie_id].update({'created_time': doc['created_time'
                    ]})
            response[storie_id].update({'updated_time': doc['updated_time'
                    ]})
            response[storie_id].update({'title': doc['title']})
            response[storie_id].update({'description': doc['description'
                    ]})
            response[storie_id].update({'location': doc['location']})
            response[storie_id].update({'visibility': doc['visibility'
                    ]})
            response[storie_id].update({'multimedia': doc['multimedia'
                    ]})
            response[storie_id].update({'story_type': doc['story_type'
                    ]})

        return response.values()

    @staticmethod
    def get_stories_by_user_id(user_id):
        stories_id = []
        response = {}
        db = MongoController.get_mongodb_instance(MONGODB_USER,
                MONGODB_PASSWD)
        stories_regs = db.users_stories.find({'user_id': user_id})
        for doc in stories_regs:
            storie_id = str(doc['storie_id'])
            response[storie_id] = {'user_id': doc['user_id']}
            stories_id.append(ObjectId(doc['storie_id']))

        stories = db.stories.find({'_id': {'$in': stories_id}})
        for doc in stories:
            storie_id = str(doc['_id'])
            response[storie_id].update({'_id': storie_id})
            response[storie_id].update({'_rev': doc['_rev']})
            response[storie_id].update({'created_time': doc['created_time'
                    ]})
            response[storie_id].update({'updated_time': doc['updated_time'
                    ]})
            response[storie_id].update({'title': doc['title']})
            response[storie_id].update({'description': doc['description'
                    ]})
            response[storie_id].update({'location': doc['location']})
            response[storie_id].update({'visibility': doc['visibility'
                    ]})
            response[storie_id].update({'multimedia': doc['multimedia'
                    ]})
            response[storie_id].update({'story_type': doc['story_type'
                    ]})

        return response.values()
