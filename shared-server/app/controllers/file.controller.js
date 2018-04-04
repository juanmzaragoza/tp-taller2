'use strict'

var FireBaseService = require("../services/fire.base.service")

class FileController{
    constructor(){
        this.get = (req, res, next)=>{
            res.status(200);
            res.json({file: 'file.jpg'})
        }
        this.post = (req, res, next)=>{
            res.status(200);
            res.json({file: 'file.jpg'})
        }
    }
}

module.exports = new FileController(); 
