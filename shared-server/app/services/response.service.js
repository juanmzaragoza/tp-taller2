"use strict";

var ResEnum = require('../common/response.enum')
var config = require('../../config/default')
var createError = require('http-errors')

class ResponseService {
    constructor() {
        this.errors = {
            "401": {
                name:"Unautorized",
                message: "Unautorized"
            },
            "500":{
                name:"Unexpected error",
                message: "Unexpected error"
            }
        };
        this.ok = (type, key, val, res, next) => {
            var data = {};
            data["metadata"] = {"version": config.server.version }
            data[key] = val;
            if(ResEnum.Value == type){ }
            else if(ResEnum.Values == type){
                data["metadata"]["total"] = val.length;
            }
            else{ data = { "code": 0, "message": "No especifico el tipo." } }
            res.json(data);
            next();
        };
        this.error = (code, res, next) =>{
            switch(code){
                case 400: next(new createError.BadRequest()); break;
                case 401: next(new createError.Unauthorized()); break;
                case 500: next(new createError.ExpectationFailed()); break;
                default:  next(new createError.ExpectationFailed());
            }
            
        }
    }
}
module.exports = new ResponseService();


