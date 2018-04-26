"use strict";
const localStorage  = require('localStorage')
const JsonStorage   = require('json-storage').JsonStorage
const uuidv4        = require('uuid/v4')
const AuthService   = require('./auth.service')

class StorageService {
    constructor() {
        this.store = JsonStorage.create(localStorage, 'user-namespace', 
        { 
            stringify: true 
        });
        this.save = (key, entity, cb) => {
            try{
                var me = this;
                entity.id =  uuidv4().toUpperCase();
                var entities = me.store.get(key);
                if(entities){
                    entities.push(entity);
                    me.store.set(key, entities);
                }
                else
                {
                    me.store.set(key, [entity]);
                }
                cb(undefined, entity.id);
            }
            catch(e){
                cb(e);
            }
        };
        this.load = (key, keySearch, value, cb) =>{
            try{
                var entities = this.store.get(key);
                var entity = undefined;
                if(entities){
                    entity = entities.find(item => item[keySearch] == value)
                    if(entity){
                        cb(undefined,entity);
                    }
                    else{
                        cb({code: -1, message: "not found"});
                    }
                }
                else{
                    cb({code: -1, message: "not found"});
                }
            }
            catch(e){
                cb(e);
            }
        }
        this.loadAll = (key, cb) =>{
            try{
                var entities = this.store.get(key);
                if(entities){
                    cb(undefined,entities);
                }
                cb(undefined,[]);
            }
            catch(e){
                cb(e);
            }
        }
        this.delete = (key, id, cb) =>{
            try{ 
                var me = this;
                var entities = this.store.get(key);
                me.load(key, 'id', id, (err, entityToDelete)=>{
                    var newEntities = [];
                    if(err){
                        cb(err)
                    }
                    else{
                        entities.forEach(entity => {
                            if(entity["id"] != entityToDelete.id){
                                newEntities.push(entity)
                            }
                        });
                    }
                    me.store.set(key, newEntities);
                    cb(undefined, id)
                })
            }
            catch(e){
                cb(e);
            }
        }
        this.update = (key, entity, cb) =>{
            try{
                var me = this
                var entities = this.store.get(key);
                var newList = []
                if(entities){
                    entities.forEach(entitySave => {
                        if(entitySave.id != entity.id){
                            newList.push(entitySave)
                        }
                        else{
                            newList.push(entity)
                        }
                    });
                    me.store.set(key, newList);
                    cb(undefined, entity);
                }
                else{
                    cb({code: -1, message: "not found"});
                }
            }
            catch(e){
                cb(e);
            }
        }
    }
}
module.exports = new StorageService();