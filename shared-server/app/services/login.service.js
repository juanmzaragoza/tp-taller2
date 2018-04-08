"use strict";
const AuthService   = require('./auth.service')
const StorageServ   = require('./storage.service')
const bcrypt        = require('bcrypt');


class LoginService {
    constructor() {
        this.auth = ( username, password, next) => {
            StorageServ.load('user', "username", username, (usr)=>{
                if(usr){
                    bcrypt.compare(password, usr.password, function(err, res) {
                        var result = undefined;
                        if(res == true){
                            result =  { 
                                token: AuthService.token(usr),
                                expiresAt: 3600
                            };
                        }
                        next(result);
                    });
                }
                else{
                    next(undefined);
                }
            });
        };
    }
}
module.exports = new LoginService();