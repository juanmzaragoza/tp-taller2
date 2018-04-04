"use strict";
var AuthService = require('./auth.service')


class LoginService {
    constructor() {
        this.auth = ( user, psw, next) => {
            var result = undefined;
            if(user == psw){
                result =  { 
                    token: AuthService.token(user),
                    expiresAt: 3600
                };
            }
            next(result);
        };
    }
}
module.exports = new LoginService();