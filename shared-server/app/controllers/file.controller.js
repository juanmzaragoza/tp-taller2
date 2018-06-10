'use strict'

const FireBaseService   = require("../services/fire.base.service")
const ResServ           = require('../services/response.service')
const ResEnum           = require('../common/response.enum')
const messages          = require('../../config/messages')
const FileService       = require('../services/file.service')

class FileController{
    constructor(){
        this.get = (req, res, next)=>{
            FileService.get(req.models)
            .then(files => {
                ResServ.ok(ResEnum.Values, "files", files, res, next);
            })
            .catch(e => {
                handleError(e, res);
            });
        }

        this.getById = (req, res, next) => {
            FileService.getById(req.params.id, req.models)
            .then(file => {
                ResServ.ok(ResEnum.Values, "server", file, res, next);
            })
            .catch(e => {
                handleError(e, res);
            });
        };

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
                    ResServ.error(res, messages.NotFound);
                }
            });
        }

        this.create = (req, res, next)=>{
            var attrs = req.body;
            FileService.add(attrs, req.models)
            .then(file => {
                ResServ.ok(ResEnum.Value, "file", file, res, next)
            })
            .catch(e => {
                handleError(e, res);
            });
        }

        this.update = (req, res, next) => {
            var id = req.params.id;
            var attrs = req.body;
            FileService.update(id, attrs, req.models)
            .then(file => {
                ResServ.ok(ResEnum.Value, "file", file, res, next);
            })
            .catch(e => {
                handleError(e, res);
            });
        };

        this.delete = (req, res, next) => {
            var id = req.params.id;
            FileService.delete(id, req.models)
            .then(() => {
                ResServ.deleteSuccessfull(res, next);
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
    }
}

module.exports = new FileController(); 
