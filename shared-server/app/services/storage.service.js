"use strict";
const localStorage  = require('localStorage')
const JsonStorage   = require('json-storage').JsonStorage
const uuidv4        = require('uuid/v4');
const bcrypt        = require('bcrypt');
const config        = require('../../config/default')
const AuthService   = require('./auth.service')

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
        }
    }
}
module.exports = new StorageService();