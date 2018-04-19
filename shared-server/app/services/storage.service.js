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
                    var newEntities;
                    if(err){
                        cb(err)
                    }
                    else{
                        newEntities = entities.find(entity => entity["id"] != entityToDelete.id)
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
                var entityOld = undefined;
                if(entities){
                    entityOld = entities.find(item => item["id"] == entity.id)
                    if(entityOld){
                        entityOld.createdBy = entity.createdBy
                        entityOld.createdTime = entity.createdTime
                        entityOld.name = entity.name
                        entityOld.host = entity.host
                        entityOld.port = entity.port
                        entityOld.pingUrl = entity.pingUrl
                        me.store.set(key, entities);
                        cb(undefined, entity);
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

        /*
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
        this.saveServer  = (key, entity, cb) => {
            var me = this;
            entity.id =  uuidv4().toUpperCase();
            entity[token]  =  { 
                token: AuthService.token(entity.name),
                expiresAt: 3600
            };
            var arr = me.store.get(key);
            if(arr){
                arr.push(entity);
                me.store.set(key, arr);
            }
            else
            {
                me.store.set(key, [entity]);
            }
            cb(entity.id);
        };
        this.load = (key, keySearch, value, cb) =>{
            var arr = this.store.get(key);
            var entity = undefined;
            if(arr){
                entity = arr.find(item => item[keySearch] == value)
            }
            cb(entity);
        }
        this.delete = (key, id, cb) =>{
            var me = this;
            var arr = []
            me.load(key, 'id', id, (entityToDelete)=>{
                console.log(entityToDelete)
                me.loadAll(key, (entities)=>{
                    entities.forEach(entity => {
                        if(entity.id != entityToDelete.id){
                            arr.push(entity)
                        }
                    })
                    me.store.set(key, arr);
                    cb("ok")
                })
            })
            cb(undefined)
        }
        this.loadAll = (key, cb) =>{
            var arr = this.store.get(key);
            if(arr){
                cb(arr);
            }
            cb([]);
        }*/
    }
}
module.exports = new StorageService();