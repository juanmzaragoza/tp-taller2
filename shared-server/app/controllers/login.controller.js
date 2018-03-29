"use strict";

var loginServ = require("../services/login.service")

class LoginController {
    constructor() {
        this.token = (req, res, next) => {
            loginServ.auth(req.body.username,req.body.password, (ticket)=>{
                if(ticket){
                    res.json(ticket);
                }
                else{
                    res.status(401);
                    res.send('user or password incorrect');
                }
                next();
            });
        };
    }
}
module.exports = new LoginController();