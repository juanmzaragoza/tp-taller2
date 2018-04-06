"use strict";
const localStorage  = require('localStorage')
const JsonStorage   = require('json-storage').JsonStorage
const uuidv4        = require('uuid/v4');

class StorageService {
    constructor() {
        this.store = JsonStorage.create(localStorage, 'user-namespace', 
        { 
            stringify: true 
        });
        this.save = (key, entity, cb) => {
            entity.id =  uuidv4().toUpperCase();
            var arr = this.store.get(key);
            if(arr){
                arr.push(entity);
                this.store.set(key, arr);
            }
            else
            {
                this.store.set(key, [entity]);
            }
            cb(entity.id);
        };
        this.load = (key, id, cb) =>{
            var arr = this.store.get(key);
            var usr;
            if(arr){
                usr = arr.find(item => item.id == id)
            }
            else{
                throw new Error("the key not exist")
            }
            cb(usr);
        }
    }
}
module.exports = new StorageService();