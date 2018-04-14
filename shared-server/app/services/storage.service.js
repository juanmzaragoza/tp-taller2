"use strict";
const localStorage  = require('localStorage')
const JsonStorage   = require('json-storage').JsonStorage
const uuidv4        = require('uuid/v4');
const bcrypt        = require('bcrypt');
const config        = require('../../config/default')

class StorageService {
    constructor() {
        this.store = JsonStorage.create(localStorage, 'user-namespace', 
        { 
            stringify: true 
        });
        this.save = (key, entity, cb) => {
            var me = this;
            entity.id =  uuidv4().toUpperCase();
            bcrypt.hash(entity.password, config.bcrypt.saltRounds, function(err, hash) { 
                entity.password = hash;
                var arr = me.store.get(key);
                if(arr){
                    arr.push(entity);
                    me.store.set(key, arr);
                }
                else
                {
                    me.store.set(key, [entity]);
                }
                delete entity.password
                cb(entity.id);
            });
        };
        this.load = (key, keySearch, value, cb) =>{
            var arr = this.store.get(key);
            var entity = undefined;
            if(arr){
                entity = arr.find(item => item[keySearch] == value)
            }
            cb(entity);
        }
    }
}
module.exports = new StorageService();