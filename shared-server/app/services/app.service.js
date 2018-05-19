'use strict'
const RemoteServ  = require('../services/remote.service')
const ServerServ  = require('../services/server.service')


class AppService {
    constructor(){
        this.stats = (id)=>{
            return new Promise((resolve, reject)=>{
                ServerServ.getById(id,(err,server)=>{
                    var url = server.host + server.stats
                    console.info("url:", url)
                    RemoteServ.get(url)
                    .then(res=>{
                        resolve(res.stats)
                    })
                    .catch(reject)
                })
            })
        }
        this.ping = (id)=>{
            return new Promise((resolve, reject)=>{
                ServerServ.getById(id,(err,server)=>{
                    var url = server.host + server.ping
                    console.log(url)
                    RemoteServ.get(url)
                    .then(res=>{
                        resolve(res.server)
                    })
                    .catch(reject)
                })
            })
        }
    }
}

module.exports = new AppService();
