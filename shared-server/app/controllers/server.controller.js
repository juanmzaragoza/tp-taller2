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
            /*StorageServ.saveServer('server',srv, (id)=>{
                if(id){
                    srv.id = id;
                    ResServ.ok(ResEnum.Value, "server", srv, res, next);
                }
                else{
                    ResServ.error(500, 2, messages.common.error, res, next);
                }
            });*/
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
            /*StorageServ.loadAll('server', (arr)=>{
                if(arr){
                    ResServ.ok(ResEnum.Values, "servers", arr, res, next);
                }
                else{
                    ResServ.error(500, 2, messages.common.error, res, next);
                }
            });*/
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