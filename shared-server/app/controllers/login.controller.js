"use strict";

var loginServ   = require("../services/login.service")
var config      = require('../../config/default')
var messages    = require('../../config/messages')
var ResServ     = require('../services/response.service')
var ResEnum     = require('../common/response.enum')
 
class LoginController {
    constructor() {
        this.token = (req, res, next) => {
            loginServ.auth(req.body.username,req.body.password, (err, ticket)=>{
                if(err){
                    var msg = "wrong user or password"
                    ResServ.error(res, messages.Unauthorized, msg);
                }
                else{
                    ResServ.ok(ResEnum.Value, "token", ticket, res, next)
                }
            });
        };
    }
}
module.exports = new LoginController();