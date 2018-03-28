"use strict";

class LoginController {
    constructor() {
        this.token = (req, res, next) => {
            res.json({token: '0923U45NI2J45HUGHFWE94U839H'});
            next();
        };
        //intance services
    }
}
module.exports = new LoginController();