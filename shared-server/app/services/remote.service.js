'use strict'
var request = require('request');


class RemoteService {
    constructor(){
        this.get = (url)=>{
            return new Promise((resolve, reject)=>{
                request.get({ url: url }, 
                (err, httpResponse, body) => {
                    if(err){ reject(err); }
                    else{ resolve(JSON.parse(body)); }
                })
            })
        }
    }
}

module.exports = new RemoteService();
