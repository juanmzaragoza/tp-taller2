"use strict";
const AuthService       = require('./auth.service')
const StorageServ       = require('./storage.service')
const DaoService    = require('../services/dao.service')
var _               = require("underscore")


class ServerService {
    constructor() {
        // this.add = (server, cb)=>{
        //     server["token"]  =  { 
        //         token: AuthService.token(server.name),
        //         expiresAt: 3600
        //     };
        //     StorageServ.save("server", server, (err, id)=>{
        //         if(err){
        //             console.log(err)
        //             cb(err);
        //         }else{
        //             cb(undefined, server);
        //         }
        //     })
        // }

        function validateAttrs(attrs) {
            if (attrs['_rev']){
                attrs.rev = attrs['_rev'];
                delete attrs['_rev'];
            }
            
            const neededAttrs = [
                "name",
                "createdBy"
            ];

            var keys = _.keys(attrs);
            var diff = _.difference(neededAttrs, keys);
            return new Promise((resolve, reject) => {
                if (diff.length == 0){
                    resolve(attrs); 
                } else {
                    reject("invalid-attrs");
                }
            });
        }

        function createServer(attrs, models) {
            attrs.createdTime = Date.now();
            return DaoService.insert(models.app_server, attrs);
        }

        this.add = (attrs, models) => {
            
            return new Promise((resolve, reject) => {
                validateAttrs(attrs)
                .then(function(attrs) {
                    return createServer(attrs, models);
                })
                .then(function(appServer){
                    var responseData = appServer.toJSON();
                    resolve(responseData);
                })
                .catch(function(err){
                    reject(err);
                })
            });
        };


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
        // this.getById = (id, cb)=>{
        //     StorageServ.load("server", "id", id, (err, server)=>{
        //         if(err){
        //             console.log(err)
        //             cb(err);
        //         }else{
        //             cb(undefined, server);
        //         }
        //     })
        // }

        function getServerReturnData(appServer){
            var data = appServer.toJSON();
            data['_rev'] = data.rev;

            return _.pick(data, ['id','_rev','createdBy','createdTime','name','lastConnection']) ;
        }

        this.getById = (id, models) => {
            return new Promise((resolve, reject) => {
                DaoService.findAll(models.app_server)
                .then( function(appServers) {
                    var serversJson = appServers.map(function(appServer) {
                        return getServerReturnData(appServer);
                    });
                    resolve(serversJson);
                })
                .catch(function(err){
                    reject(err);
                })
            });
        }

        this.get = (models) => {
            return new Promise((resolve, reject) => {
                DaoService.findAll(models.app_server)
                .then( function(appServers) {
                    var serversJson = appServers.map(function(appServer) {
                        return getServerReturnData(appServer);
                    });
                    resolve(serversJson);
                })
                .catch(function(err){
                    reject(err);
                })
            });
        }
    }
}
module.exports = new ServerService();