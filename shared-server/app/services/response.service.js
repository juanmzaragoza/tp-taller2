"use strict";
var _ = require("underscore")
var ResEnum = require('../common/response.enum')
var config = require('../../config/default')
var createError = require('http-errors')
const messages      = require('../../config/messages')

class ResponseService {
    constructor() {
        this.ok = (type, key, val, res, next) => {
            var data = {};
            var status = 200
            data["metadata"] = {"version": config.server.version }
            data[key] = val;
            if(ResEnum.Value == type){ }
            else if(ResEnum.Values == type){
                data["metadata"]["total"] = val.length;
            }
            else if(ResEnum.OnlyValue == type){
                data = val;
            }
            else{ data = { "code": 0, "message": "No especifico el tipo." } }
            res.status(status);
            res.json(data);
            next();
        };
        this.error = (res, err, msg) =>{
            var body = _.clone(err.body)
            if(msg) body.message = msg
            res.status(err.codeHttp).send(body);
        };

        this.deleteSuccessfull = (res, next) => {
            res.status(204).send();
        };

        this.errorNotFound = (res) => {
            const err = messages.NotFound;
            this.error(res, err);
        };

        this.errorBadRequest = (res) => {
            const err = messages.BadRequest;
            this.error(res, err);
        };

        this.errorConflict = (res) => {
            const err = messages.Conflict;
            this.error(res, err);
        }

        this.errorInternal = (res) => {
            const err = messages.InternalServerError;
            this.error(res, err);   
        }
    }
}
module.exports = new ResponseService();


