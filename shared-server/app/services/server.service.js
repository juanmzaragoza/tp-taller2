"use strict";
const AuthService       = require('./auth.service')
const StorageServ       = require('./storage.service')


class ServerService {
    constructor() {
        this.add = (server, cb)=>{
            server["token"]  =  { 
                token: AuthService.token(server.name),
                expiresAt: 3600
            };
            StorageServ.save("server", server, (err, id)=>{
                if(err){
                    console.log(err)
                    cb(err);
                }else{
                    cb(undefined, server);
                }
            })
        }
        this.refreshToken = (id, cb)=>{
            var me = this
            me.getById(id, (err, server)=>{
                if(err){
                    console.log(err)
                    cb(err);
                }else{
                    server["token"]  =  { 
                        token: AuthService.token(server.name),
                        expiresAt: 3600
                    };
                    StorageServ.update("server", server, (err, server)=>{
                        if(err){
                            console.log(err)
                            cb(err);
                        }else{
                            cb(undefined, server);
                        }
                    })
                }
            })
        }
        this.update = (server, cb)=>{
            var me = this
            me.getById(server.id, (err, serverOld)=>{
                server.token = serverOld.token
                StorageServ.update("server", server, (err, server)=>{
                    if(err){
                        console.log(err)
                        cb(err);
                    }else{
                        cb(undefined, server);
                    }
                })
            })
        }
        this.delete = (id, cb)=>{
            StorageServ.delete("server", id, (err, id)=>{
                if(err){
                    console.log(err)
                    cb(err);
                }else{
                    cb(undefined, "Baja correcta");
                }
            })
        }
        this.getById = (id, cb)=>{
            StorageServ.load("server", "id", id, (err, server)=>{
                if(err){
                    console.log(err)
                    cb(err);
                }else{
                    cb(undefined, server);
                }
            })
        }
        this.get = (cb)=>{
            StorageServ.loadAll("server", (err, servers)=>{
                if(err){
                    console.log(err)
                    cb(err);
                }else{
                    cb(undefined, servers);
                }
            })
        }
    }
}
module.exports = new ServerService();