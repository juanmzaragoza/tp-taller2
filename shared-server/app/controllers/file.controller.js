'use strict'

class FileController{
    constructor(){
        this.get = (req, res, next)=>{
            res.status(200);
            res.json({file: 'file.jpg'})
        }
    }
}

module.exports = new FileController(); 
