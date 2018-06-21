const config        = require('../../config/default')
const messages      = require('../../config/messages')
const AuthServ      = require('../services/auth.service')
const ResServ       = require('../services/response.service')
const ResEnum       = require('../common/response.enum')
const ServerService = require('../services/server.service')
const UserService   = require('../services/user.service')

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

        function getCurrentUser(req) {
            return new Promise((resolve, reject) => {
                var token = AuthServ.getTokenFromRequest(req);
                UserService.getUserForToken(token, req.models)
                .then(user => {
                    resolve(user);
                })
                .catch(e => {
                    console.log(e);
                    reject('internal');
                });
            });
        }

        this.post = (req, res, next) => {
            getCurrentUser(req)
            .then(user => {
                var serverAttrs = req.body;
                return ServerService.add(serverAttrs, req.models, user);
            })
            .then(appServer=>{
                ResServ.ok(ResEnum.Value, "server", appServer, res, next)
            })
            .catch(e =>{
                console.log("e:" + e);
                handleError(e, res);
            })
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
        this.ping = (req, res, next) => {
            try{
                var id = req.params.id;
                ServerService.ping(id, req.models)
                .then((ping) => {
                    console.log(ping)
                    ResServ.ok(ResEnum.Value, "ping", ping, res, next);
                })
                .catch((e) => {
                    console.log(e)
                    ResServ.error(res, messages.BadRequest);
                });
            }
            catch(e){
                console.log(e)
                ResServ.error(res, messages.InternalServerError);
            }
        };
        this.stats = (req, res, next) => {
            try{
                var id = req.params.id;
                ServerService.stats(id, req.models)
                .then((stats) => {
                    console.log(stats)
                    stats["_id"] = id;
                    ResServ.ok(ResEnum.Value, "stats", stats, res, next);
                })
                .catch((e) => {
                    console.log(e)
                    ResServ.error(res, messages.BadRequest);
                });
            }
            catch(e){
                console.log(e)
                ResServ.error(res, messages.InternalServerError);
            }
        };

        this.requests = (req, res, next) => {
            try{
                var id = req.params.id;
                ServerService.requests(id, req.query, req.models)
                .then((requests) => {
                    ResServ.ok(ResEnum.Value, "requests", requests, res, next);
                })
                .catch((e) => {
                    console.log(e)
                    ResServ.error(res, messages.BadRequest);
                });
            }
            catch(e){
                console.log(e)
                ResServ.error(res, messages.InternalServerError);
            }
        };
    }
}
module.exports = new ServerController();