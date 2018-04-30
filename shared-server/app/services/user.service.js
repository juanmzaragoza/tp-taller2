"use strict";
const AuthService   = require('./auth.service')
const StorageServ   = require('./storage.service')
const bcrypt        = require('bcrypt')
const config        = require('../../config/default')
const DaoService    = require('../services/dao.service')


class UserService {
    constructor() {
        this.add = (user, models)=>{
            return new Promise((resolve, reject)=>{
                user["token"]  =  { 
                    token: AuthService.token(user),
                    expiresAt: 3600
                };
                bcrypt.hash(user.password, config.bcrypt.saltRounds, function(err, hash) { 
                    user.password = hash;
                    user.role = "app"
                    DaoService.insert(models.user, user)
                    .then(newUser =>{
                        delete newUser.password
                        resolve(newUser)
                    })
                    .catch(e=>{
                        reject(e)
                    })
                })
            })
            
        }
        this.getById = (id, cb)=>{
            StorageServ.load("user", "id", id, (err, user)=>{
                if(err){
                    cb(err);
                }else{
                    cb(undefined, user);
                }
            })
        }
    }
}
module.exports = new UserService();