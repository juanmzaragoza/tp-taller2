const config      = require('../../config/default')
const messages    = require('../../config/messages')
const StorageServ = require('../services/storage.service')
const AuthServ    = require('../services/auth.service')
const User        = require('../models/user')
const ResServ     = require('../services/response.service')
const ResEnum     = require('../common/response.enum')

class UserController {
    constructor() {
        this.user = (req, res, next) => {
            var usr = new User(req.body);
            StorageServ.save('user',usr, (id)=>{
                if(id){
                    usr.id = id;
                    usr["token"] = AuthServ.token(usr)
                    ResServ.ok(ResEnum.Value, "user", usr, res, next);
                }
                else{
                    ResServ.error(500, 2, messages.common.error, res, next);
                }
            });
        };
        this.getById = (req, res, next) => {
            var id = req.params.id;
            StorageServ.load('user', "id", id, (usr)=>{
                if(usr){
                    ResServ.ok(ResEnum.Value, "user", usr, res, next);
                }
                else{
                    ResServ.error(404, 1, messages.user.notExist, res, next);
                }
            });
        };
    }
}
module.exports = new UserController();
