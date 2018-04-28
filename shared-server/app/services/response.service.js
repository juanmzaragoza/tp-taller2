"use strict";

var ResEnum = require('../common/response.enum')
var config = require('../../config/default')
var createError = require('http-errors')

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
            var body = err.body
            if(msg){
                body.message = msg
            }
            res.status(err.codeHttp).send(body);
        }
    }
}
module.exports = new ResponseService();


