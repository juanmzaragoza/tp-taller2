"use strict";

var loginServ   = require("../services/login.service")
var config      = require('../../config/default')
var messages    = require('../../config/messages')
var ResServ     = require('../services/response.service')
var ResEnum     = require('../common/response.enum')
 
class LoginController {
    constructor() {
        this.token = (req, res, next) => {
            loginServ.auth(req.body.username,req.body.password, req.models)
            .then((ticket) => {
                ResServ.ok(ResEnum.Value, "token", ticket, res, next);
            })
            .catch((reason) => {
                console.log('Handle rejected promise ('+reason+') here.');
                if (reason == 'unauthorized'){
                    ResServ.error(401, "err", messages.user.wrong, res, next);    
                } else {
                    ResServ.error(500, "err", messages.common.error, res, next);    
                }
            });
        };
    }
}
module.exports = new LoginController();