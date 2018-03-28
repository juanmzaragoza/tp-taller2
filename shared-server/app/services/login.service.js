"use strict";



class LoginService {
    constructor() {
        this.auth = (req, user, psw, next) => {
            var result = undefined;
            if(user == psw){
                result =  { token: '0923U45NI2J45HUGHFWE94U839H'};
            }
            next({test: req});
        };
    }
}
module.exports = new LoginService();