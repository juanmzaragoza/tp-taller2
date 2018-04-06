var config      = require('../../config/default')
var StorageServ = require('../services/storage.service')
var User        = require('../models/user')

class UserController {
    constructor() {
        this.user = (req, res, next) => {
            var usr = new User(req.body);
            StorageServ.save('user',usr, (id)=>{
                if(id){
                    res.json({
                        metadata:{
                            version: config.server.version
                        },
                        "user": {
                            "id": id,
                            "_rev": "string",
                            "applicationOwner": usr.applicationOwner,
                            "username": usr.username
                          }
                    });
                }
                else{
                    res.status(500);
                    res.send('Error');
                }
                next();
            });
        };
        this.getById = (req, res, next) => {
            var id = req.params.id;
            StorageServ.load('user',id, (usr)=>{
                if(usr){
                    res.json({
                        metadata:{
                            version: config.server.version
                        },
                        "user": {
                            "id": usr.id,
                            "_rev": "string",
                            "applicationOwner": usr.applicationOwner,
                            "username": usr.username
                          }
                    });
                }
                else{
                    res.status(500);
                    res.send('user not exist');
                }
                next();
            });
        };
    }
}
module.exports = new UserController();
