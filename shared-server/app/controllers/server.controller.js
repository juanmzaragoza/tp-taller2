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
            var srv = new Server(req.body);
            ServerService.add(srv, (err, server)=>{
                if(err){
                    ResServ.error(404, 2, messages.common.error, res, next);
                }
                else{
                    ResServ.ok(ResEnum.Value, "server", server, res, next);
                }
            })
        };
        this.get = (req, res, next) => {
            ServerService.get((err, servers)=>{
                if(err){
                    ResServ.error(404, 2, messages.common.error, res, next);
                }
                else{
                    ResServ.ok(ResEnum.Values, "servers", servers, res, next);
                }
            })
        };
        this.refreshToken = (req, res, next) => {
            ServerService.refreshToken(req.params.id, (err, server)=>{
                if(err){
                    ResServ.error(404, 2, messages.common.error, res, next);
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
                    ResServ.error(404, 2, messages.common.error, res, next);
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
                    ResServ.error(404, 2, messages.common.error, res, next);
                }
                else{
                    ResServ.ok(ResEnum.OnlyValue, undefined, message, res, next);
                }
            })
        };
    }
}
module.exports = new ServerController();