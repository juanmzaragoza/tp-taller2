'use strict'

var FireBaseService = require("../services/fire.base.service")

class FileController{
    constructor(){
        this.get = (req, res, next)=>{
            res.status(200);
            res.json({file: 'file.jpg'})
        }
        this.post = (req, res, next)=>{
            FireBaseService.upload(req.body.resource, (err, url) =>{
                res.status(200);
                res.json({url: url})
            });
        }
    }
}

module.exports = new FileController(); 
