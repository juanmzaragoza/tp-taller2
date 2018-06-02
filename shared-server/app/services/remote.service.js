'use strict'
var request = require('request');


class RemoteService {
    constructor(){
        this.get = (url)=>{
            return new Promise((resolve, reject)=>{
                request.get({ url: url }, 
                (err, httpResponse, body) => {
                    if(err){ reject(err); }
                    else{ 
                        try{
                            resolve(JSON.parse(body)); 
                        }
                        catch(e){
                            reject(err);
                        }
                    }
                })
            })
        }
    }
}

module.exports = new RemoteService();
