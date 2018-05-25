const config        = require('../../config/default')
const messages      = require('../../config/messages')
const AuthServ      = require('../services/auth.service')
const Server        = require('../models/server')
const ResServ       = require('../services/response.service')
const ResEnum       = require('../common/response.enum')
const ServerService = require('../services/server.service')

class ServerController {
    constructor() {
       
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

        this.getById = (req, res, next)=>{
            ServerService.getById(req.params.id, (err, server)=>{
                if(err){
                    ResServ.error(res, messages.NotFound);
                }
                else{
                    ResServ.ok(ResEnum.Values, "server", server, res, next);
                }
            })
        }

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
        this.put = (req, res, next) => {
            var srv = new Server(req.body);
            srv.id = req.params.id;
            ServerService.update(srv, (err, server)=>{
                if(err){
                    ResServ.error(res, messages.NotFound);
                }
                else{
                    ResServ.ok(ResEnum.Value, "server", server, res, next);
                }
            })
        };
        this.delete = (req, res, next) => {
            var id = req.params.id;
            ServerService.delete(id, (err, message)=>{
                console.info(err)
                if(err){
                    ResServ.error(res, messages.NotFound);
                }
                else{
                    ResServ.ok(ResEnum.OnlyValue, undefined, message, res, next);
                }
            })
        };
    }
}
module.exports = new ServerController();