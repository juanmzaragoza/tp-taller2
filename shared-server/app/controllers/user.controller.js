const config      = require('../../config/default')
const messages    = require('../../config/messages')
const AuthServ    = require('../services/auth.service')
const User        = require('../models/user')
const ResServ     = require('../services/response.service')
const ResEnum     = require('../common/response.enum')
const UserService = require('../services/user.service')

class UserController {
    constructor() {
        this.user = (req, res, next) => {
            try{
                var usr = new User(req.body);
                UserService.add(usr,(err, user)=>{
                    if(err){
                        ResServ.error(404, 10, "Not Found", res, next);
                    }
                    else{
                        ResServ.ok(ResEnum.Value, "user", user, res, next);
                    }
                })
            }
            catch(e){
                ResServ.error(500, 10, "Unexpected error", res, next);
            }
        };
        this.getById = (req, res, next) => {
            var id = req.params.id;
            UserService.getById(id,(err, user)=>{
                if(err){
                    ResServ.error(404, 2, messages.common.error, res, next);
                }
                else{
                    ResServ.ok(ResEnum.Value, "user", user, res, next);
                }
            })
        };
    }
}
module.exports = new UserController();
