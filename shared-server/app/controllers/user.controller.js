const config      = require('../../config/default')
const messages    = require('../../config/messages')
const AuthServ    = require('../services/auth.service')
const ResServ     = require('../services/response.service')
const ResEnum     = require('../common/response.enum')
const UserService = require('../services/user.service')
const ServerService = require('../services/server.service')

class UserController {
    constructor() {
        this.user = (req, res, next) => {
            try{
                ServerService.updateLastConnection(req.get('api-key'), req.models)
                .then(() => {
                    var userAttrs = req.body;
                    userAttrs.role = "app";
                    return UserService.add(userAttrs, req.models);
                })
                .then(user=>{
                    ResServ.ok(ResEnum.Value, "user", user, res, next)
                })
                .catch(e =>{
                    if(e == "SequelizeUniqueConstraintError"){
                        var msg = "Conflict: the username already exists"
                        ResServ.error(res, messages.Conflict,msg)
                    }
                    else{
                        ResServ.error(res, messages.BadRequest)
                    }
                })
            }
            catch(e){
                console.log(e)
                ResServ.error(res, messages.InternalServerError);
            }

        };
        
        this.getById = (req, res, next) => {
            var id = req.params.id;
            UserService.getById(id,(err, user)=>{
                if(err){
                    ResServ.error(res, messages.NotFound);
                }
                else{
                    ResServ.ok(ResEnum.Value, "user", user, res, next);
                }
            })
        };
    }
}
module.exports = new UserController();
