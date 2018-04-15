const config      = require('../../config/default')
const messages    = require('../../config/messages')
const StorageServ = require('../services/storage.service')
const AuthServ    = require('../services/auth.service')
const Server      = require('../models/server')
const ResServ     = require('../services/response.service')
const ResEnum     = require('../common/response.enum')

class ServerController {
    constructor() {
        this.post = (req, res, next) => {
            var srv = new Server(req.body);
            StorageServ.saveServer('server',srv, (id)=>{
                if(id){
                    srv.id = id;
                    ResServ.ok(ResEnum.Value, "server", srv, res, next);
                }
                else{
                    ResServ.error(500, 2, messages.common.error, res, next);
                }
            });
        };
        this.get = (req, res, next) => {
            StorageServ.loadAll('server', (arr)=>{
                if(arr){
                    ResServ.ok(ResEnum.Values, "servers", arr, res, next);
                }
                else{
                    ResServ.error(500, 2, messages.common.error, res, next);
                }
            });
        };
    }
}
module.exports = new ServerController();