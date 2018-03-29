"use strict";



class LoginService {
    constructor() {
        this.auth = ( user, psw, next) => {
            var result = undefined;
            if(user == psw){
                result =  { token: '0923U45NI2J45HUGHFWE94U839H'};
            }
            next(result);
        };
    }
}
module.exports = new LoginService();