const messages    = require('../../config/messages')
const ResServ     = require('../services/response.service')
const ResEnum     = require('../common/response.enum')
const AppServ     = require('../services/app.service')

class AppController {
    constructor() {
        this.stats = (req, res, next) => {
            try{
                var id = req.params.id;
                AppServ.stats(id)
                .then((stats) => {
                    console.log(stats)
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
        
        this.ping = (req, res, next) => {
            try{
                var id = req.params.id;
                AppServ.ping(id)
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
    }
}
module.exports = new AppController();
