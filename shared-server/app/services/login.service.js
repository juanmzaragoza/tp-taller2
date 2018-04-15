"use strict";
const AuthService   = require('./auth.service')
const StorageServ   = require('./storage.service')
const bcrypt        = require('bcrypt');

const USER_NOT_FOUND = "not-found";
const INVALID_PASSWORD = "invalid-password";

class LoginService {
    constructor() {
        function getUserByUsername(username, models){
            return new Promise((resolve, reject) => {
                models.user.findOne(
                    { where: {username: username} }
                ).then(function(user){
                    if (user === null) {
                        reject(USER_NOT_FOUND);
                    } else{
                        // console.log("user:",user.toJSON());    
                        resolve(user);  
                    }
                })
            });
        }

        function checkValidPassword(plainPassword, user) {
            return new Promise((resolve, reject) => {
                bcrypt.compare(plainPassword, user.password).then(function(valid) {
                    if (!valid){
                        reject(INVALID_PASSWORD);
                    } else {
                        resolve(user);    
                    }
                });
            });
        }


        this.auth = (username, password, models) => {
            return new Promise((resolve, reject) => {

                getUserByUsername(username, models)
                .then( function(user){
                    return checkValidPassword(password, user)
                })
                .then( function(user){
                    var result = {
                        token: AuthService.token(user.username),
                        expiresAt: 3600
                    };                    
                    resolve(result);
                })
                .catch(function(err){
                    var reason = 'unexpected';
                    if (err == USER_NOT_FOUND || err == INVALID_PASSWORD){
                        reason = 'unauthorized';
                    }
                    reject(reason);
                })
            });
        };
    }
}
module.exports = new LoginService();