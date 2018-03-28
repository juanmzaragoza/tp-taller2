"use strict";

var loginServ = require("../services/login.service")

class LoginController {
    constructor() {
        this.token = (req, res, next) => {
            
            res.json(req.pausername);
            /*loginServ.auth(req.body, req.username,req.password, (ticket)=>{
                if(ticket){
                    res.json(ticket);
                }
                else{
                    res.sendstatus(401);
                }
                next();
            });*/
        };
    }
}
module.exports = new LoginController();