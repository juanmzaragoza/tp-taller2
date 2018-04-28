"use strict";

var loginServ   = require("../services/login.service")
var config      = require('../../config/default')
var messages    = require('../../config/messages')
var ResServ     = require('../services/response.service')
var ResEnum     = require('../common/response.enum')
 
class LoginController {
    constructor() {
        this.token = (req, res, next) => {
            if(req.body.username && req.body.password){ 
                loginServ.auth(req.body.username,req.body.password, (err, ticket)=>{
                    if(err){
                        var msg;
                        switch(err){
                            case "unknown":
                                var msg = "wrong user or password"
                                ResServ.error(res, messages.Unauthorized, msg);
                                break;
                            case "fb_not_valid":
                                ResServ.error(res, messages.Unauthorized);
                                break;
                            case "fb_user_not_exist":
                                msg = "Facebook User doesn't exists";
                                ResServ.error(res, messages.Conflict, msg);
                                break;
                            default: ResServ.error(res, messages.Unauthorized);
                        }
                    }
                    else{
                        ResServ.ok(ResEnum.Value, "token", ticket, res, next)
                    }
                });
            }
            else{
                ResServ.error(res, messages.BadRequest);
            }
        };
    }
}
module.exports = new LoginController();