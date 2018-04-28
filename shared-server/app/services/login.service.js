"use strict";
const AuthService       = require('./auth.service')
const StorageServ       = require('./storage.service')
const LocalStorageServ  = require('./local.storage.service')
const bcrypt            = require('bcrypt');


class LoginService {
    constructor() {
        this.auth = ( username, password, next) => {
            var me = this
            if(me.isUserFB(password)){
                me.authByFB(username, password, next)
            }
            else{
                me.authByApp(username, password, next)
            }
        }

        this.isUserFB = (password)=>{
            var fb = password.split(" ")[0]
            return (fb == "fb")
        }
        
        this.authByApp = (username, password, next) => {
            var me = this;
            StorageServ.load('user', "username", username, (err, usr)=>{
                if(usr){
                    //users of the application-server
                    me.valid(usr, password, next)
                }
                else{
                    //users of the shared-server
                    LocalStorageServ.load(username, (usrAdmin) =>{
                        if(usrAdmin){
                            me.valid(usrAdmin, password, next)
                        }
                        else{
                            //unknown
                            next("unknown");
                        }
                    })
                }
            });
        };
        this.authByFB = ( username, password, next) => {
            var me = this;
            me.isValidFB(password,(err, res)=>{
                if(err){
                    next("error_fb");
                }
                else{
                    if(res == true){
                        StorageServ.load('user', "username", username, (err, usr)=>{
                            if(usr){
                                //users of the application-server
                                next(undefined, me.generateToken(user));
                            }
                            else{
                                next("fb_user_not_exist");
                            }
                        })
                    }
                    else{
                        next("fb_not_valid");
                    }
                }
            })
        };
        this.valid = (user, password, next)=>{
            var me = this
            me.isValidApp(user, password, (err, res) => {
                if(err){
                    next("error_isValidApp");
                }
                else{
                    if(res == true){
                        next(undefined, me.generateToken(user));
                    }
                    else{
                        next("unknown");
                    }
                }
            })
        }
        this.generateToken = (user)=>{
            return { token: AuthService.token(user), expiresAt: 3600};
        }
        this.isValidApp = (user, password, next) =>{
            bcrypt.compare(password, user.password, function(err, res) {
                next(err, res)
            });
        }
        this.isValidFB = (tokenFB, next) =>{
            //TODO falta impelmentar la llamada a fb
            var token = tokenFB.split(" ")[1]
            next(undefined,true)
        }
    }
}
module.exports = new LoginService();