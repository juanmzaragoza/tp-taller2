"use strict";
const AuthService       = require('./auth.service')
const StorageServ       = require('./storage.service')
const LocalStorageServ  = require('./local.storage.service')
const bcrypt            = require('bcrypt');


class LoginService {
    constructor() {
        this.auth = ( username, password, next) => {
            var me = this;
            StorageServ.load('user', "username", username, (usr)=>{
                if(usr){
                    //users of the application-server
                    me.generateToken(usr, password, next);
                }
                else{
                    //users of the shared-server
                    LocalStorageServ.load(username, (usrAdmin) =>{
                        if(usrAdmin){
                            me.generateToken(usrAdmin, password, next);
                        }
                        else{
                            //unknown
                            next(undefined);
                        }
                    })
                }
            });
        };
        this.generateToken = (user, password, next)=>{
            bcrypt.compare(password, user.password, function(err, res) {
                var result = undefined;
                if(res == true){
                    result =  { 
                        token: AuthService.token(user),
                        expiresAt: 3600
                    };
                }
                next(result);
            });
        }
    }
}
module.exports = new LoginService();