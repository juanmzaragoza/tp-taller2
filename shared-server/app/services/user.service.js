"use strict";
const AuthService   = require('./auth.service')
const StorageServ   = require('./storage.service')
const bcrypt        = require('bcrypt')
const config        = require('../../config/default')


class UserService {
    constructor() {
        this.add = (user, cb)=>{
            user["token"]  =  { 
                token: AuthService.token(user),
                expiresAt: 3600
            };
            bcrypt.hash(user.password, config.bcrypt.saltRounds, function(err, hash) { 
                user.password = hash;
                StorageServ.save("user", user, (err, id)=>{
                    if(err){
                        cb(err);
                    }else{
                        delete user.password
                        cb(undefined, user);
                    }
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