'use strict'

const FireBaseService   = require("../services/fire.base.service")
const ResServ           = require('../services/response.service')
const ResEnum           = require('../common/response.enum')
const messages          = require('../../config/messages')

class FileController{
    constructor(){
        this.get = (req, res, next)=>{
            var servers = [
                {
                    id: '1',
                    _rev: 'rev',
                    createdTime: 0,
                    updatedTime: 0,
                    size: 60,
                    filename: "file.jpg",
                    resource: "data"
                }
            ]
            ResServ.ok(ResEnum.Values, "servers", servers, res, next);
        }
        this.postUpload = (req, res, next)=>{
            FireBaseService.upload(req.body, (err, url) =>{
                if(err == undefined && url){
                    var server = {
                                id: '1',
                                _rev: 'rev',
                                createdTime: 0,
                                updatedTime: 0,
                                size: 60,
                                filename: "file.jpg",
                                resource: url
                            }
                    ResServ.ok(ResEnum.Value, "server", server, res, next);
                }
                else{
                    ResServ.error(500, messages.common.error, res, next);
                }
            });
        }
    }
}

module.exports = new FileController(); 
