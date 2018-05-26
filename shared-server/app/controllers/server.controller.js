const config        = require('../../config/default')
const messages      = require('../../config/messages')
const AuthServ      = require('../services/auth.service')
const Server        = require('../models/server')
const ResServ       = require('../services/response.service')
const ResEnum       = require('../common/response.enum')
const ServerService = require('../services/server.service')

class ServerController {
    constructor() {
       
        this.getById = (req, res, next) => {
            ServerService.getById(req.params.id, req.models)
            .then(appServer => {
                ResServ.ok(ResEnum.Values, "server", appServer, res, next);
            })
            .catch( e => {
                console.log(e);
                ResServ.error(res, messages.NotFound);
            });
        };

        this.get = (req, res, next) => {
            ServerService.get(req.models)
            .then(appServers => {
                ResServ.ok(ResEnum.Values, "servers", appServers, res, next);
            })
            .catch( e => {
                console.log(e);
                ResServ.error(res, messages.NotFound);
            });
        };

        this.refreshToken = (req, res, next) => {
            ServerService.refreshToken(req.params.id, (err, server)=>{
                if(err){
                    ResServ.error(res, messages.NotFound);
                }
                else{
                    ResServ.ok(ResEnum.Value, "server", server, res, next);
                }
            })
        }

        this.post = (req, res, next) => {
            try{
                var serverAttrs = req.body;
                
                //TODO: que agarre el user id posta
                serverAttrs.createdBy = 3; 
                
                ServerService.add(serverAttrs, req.models)
                .then(appServer=>{
                    ResServ.ok(ResEnum.Value, "server", appServer, res, next)
                })
                .catch(e =>{
                    console.log("e:" + e);
                    ResServ.error(res, messages.BadRequest)
                })
            }
            catch(e){
                console.log(e)
                ResServ.error(res, messages.InternalServerError);
            }
        };

        this.put = (req, res, next) => {
            var id = req.params.id;
            var attrs = req.body;
            ServerService.update(id, attrs, req.models)
            .then( appServer => {
                ResServ.ok(ResEnum.Value, "server", appServer, res, next);
            })
            .catch(e => {
                handleError(e, res);
            });
        };

        function handleError(e, res){
            if (e == 'not-found'){
                ResServ.errorNotFound(res);
            } else if (e == 'invalid-attrs') {
                ResServ.errorBadRequest(res);
            } else if (e == 'conflict'){
                ResServ.errorConflict(res);
            } else {
                ResServ.errorInternal(res);    
            }
        }
        
        this.delete = (req, res, next) => {
            var id = req.params.id;
            ServerService.delete(id, req.models)
            .then(() => {
                ResServ.deleteSuccessfull(res, next);
            })
            .catch(e => {
                if (e == 'not-found'){
                    ResServ.errorNotFound(res);
                } else {
                    ResServ.error(res, e);    
                }
            });
        };
    }
}
module.exports = new ServerController();