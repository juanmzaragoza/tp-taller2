'use strict'

var FireBaseService = require("../services/fire.base.service")

class FileController{
    constructor(){
        this.get = (req, res, next)=>{
            res.status(200);
            res.json({file: 'file.jpg'})
        }
        this.postUpload = (req, res, next)=>{
            FireBaseService.upload2(req.body, (err, url) =>{
                res.status(200);
                res.json({url: url})
            });
        }
    }
}

module.exports = new FileController(); 
