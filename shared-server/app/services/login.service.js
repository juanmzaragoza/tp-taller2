"use strict";
const AuthService       = require('./auth.service')
const StorageServ       = require('./storage.service')
const LocalStorageServ  = require('./local.storage.service')
const bcrypt            = require('bcrypt');

var request             = require('request');
var config              = require('../../config/default')


const USER_NOT_FOUND    = "not-found";
const INVALID_PASSWORD  = "invalid-password";
const INVALID_ACCESS_TOKEN  = "invalid-access-token";


class LoginService {
    constructor() {
        function authByFB(username, password, models){
            return new Promise((resolve, reject)=>{
                isValidFB(password)
                .then(function(valid){

                    if(valid){
                        return getUserByUsername(username, models)
                        .then( function(user){
                            var result = {
                                token: AuthService.token(user),
                                expiresAt: 3600
                            };                    
                            resolve(result);
                        })
                        .catch(function(err){
                            reject(handleErrFB(err));
                        })
                    }
                    else{
                        reject(INVALID_ACCESS_TOKEN);
                    }
                })
                .catch(function(err){
                    reject(handleErrFB(err));
                })
            });
        };
        function isValidFB(tokenFB){
            return new Promise((resolve, reject)=>{
                var token = tokenFB.split(" ")[1]
                var url = 'https://graph.facebook.com/debug_token'
                + "?input_token=" + token //"EAAexY6t49L0BAD1JqZB3m2DiPGlqaTbugtzyUSyTIB0LHUZBISfjwh8X9TI9VkChxZByVW0ZAm4S8RgBPMkP0Bq16ZAFfhBR4KztRsMfWH4CbNRfoaP0ToZBkXCa2ZADW6BtxnPc5gwGZBPAHwiWPiuZCj2SHGzvgWRWjp3dJdZB8tZBTv1HiCTyRW7ZAjq1crLyxAsZD"
                + "&access_token=" + config.auth.facebook.access_token; //"2165366473684157|2juX9BXHUZNqRZVJSGjd6gOyRzE"
                request.get({ url:url }, 
                (err, httpResponse, body) => {
                    if(err){
                        reject("error graph facebook");
                    }
                    else{
                        var res = JSON.parse(body);
                        //console.log(res.data.is_valid)
                        resolve(res.data.is_valid);
                    }
                })
            })
        };
        function getAdminUser(username){
            return new Promise((resolve, reject)=>{
                //users of the shared-server
                LocalStorageServ.load(username, (usrAdmin) =>{
                    if(usrAdmin){
                        resolve(usrAdmin);
                    }
                    else{
                        reject(USER_NOT_FOUND);
                    }
                })
            })
        };
        function authByApp(username, password, models){
            return new Promise((resolve, reject) => {
                getUserByUsername(username, models)
                .then( function(user){
                    return checkValidPassword(password, user)
                })
                .then( function(user){
                    var result = {
                        token: AuthService.token(user),
                        expiresAt: 3600
                    };
                    resolve(result);
                })
                .catch(function(err){
                    var reason = handleErrAPP(err)
                    reject(reason);
                })
            });
        };
        function getUserByUsername(username, models){
            return new Promise((resolve, reject) => {
                models.user.findOne(
                    { where: {username: username} }
                ).then(function(user){

            //console.log("hola",username, )
                    if (user === null) {
                        getAdminUser(username).then(userAdmin=>{
                            resolve(userAdmin)
                        }).catch(err =>{
                            reject(err)
                        })
                    } else{
                        // console.log("user:",user.toJSON());    
                        resolve(user);  
                    }
                }).catch(err =>{
                    reject(err)
                })
            });
        };
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
        };
        function isUserFB(password){
            var fb = password.split(" ")[0]
            return (fb == "fb")
        };

        this.auth = (username, password, models) => {
            if(isUserFB(password)){
                return authByFB(username, password, models);
            }
            else{
                return authByApp(username, password, models)
            }
        }
        function handleErrFB(err){
            var reason = handleErrAPP(err)
            if (err == USER_NOT_FOUND){
                reason = "fb_user_not_exist";
            }
            return reason
        }
        function handleErrAPP(err){
            var reason = 'unexpected';
            if (err == USER_NOT_FOUND || err == INVALID_PASSWORD){
                reason = 'unauthorized';
            }
            return reason
        }

    }
}
module.exports = new LoginService();