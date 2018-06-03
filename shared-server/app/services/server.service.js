"use strict";
const AuthService       = require('./auth.service')
const StorageServ       = require('./storage.service')
const DaoService    = require('../services/dao.service')
var _               = require("underscore")


class ServerService {
    constructor() {
        
        function validateAttrs(attrs, neededAttrs) {
            if (attrs['_rev']){
                attrs.rev = attrs['_rev'];
                delete attrs['_rev'];
            }
            
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

        function validateCreationAttrs(attrs){
            const neededAttrs = ["name","userId"];
            return validateAttrs(attrs, neededAttrs);
        }

        function validateUpdateAttrs(attrs){
            const neededAttrs = ["name","rev"];
            return validateAttrs(attrs, neededAttrs);
        }

        function createServer(attrs, models) {
            attrs.createdTime = Date.now();
            attrs.rev = new Date().getTime();
            return DaoService.insert(models.app_server, attrs);
        }

        this.add = (attrs, models, user) => {
            return new Promise((resolve, reject) => {
                attrs.userId = user.id;
                validateCreationAttrs(attrs)
                .then(function(attrs) {
                    attrs.token = AuthService.token(attrs.name);
                    return createServer(attrs, models);
                })
                .then(function(appServer){
                    appServer.User = user;
                    var serverJson = getServerReturnData(appServer);
                    var responseData = {
                        server: serverJson,
                        token: getTokenData(appServer)
                    };
                    resolve(responseData);
                })
                .catch(function(err){
                    reject(err);
                })
            });
        };

        function validateOptimisticLock(appServer, attrs){
            if (appServer.rev != attrs.rev){
                throw "conflict";
            }
        }

        function updateAttrsForServer(appServer, attrs) {
            var attrsToUpdate = _.pick(attrs, ['name']) ;
            for (var key in attrsToUpdate){
                appServer[key] = attrsToUpdate[key];
            }
            appServer.rev = new Date().getTime();
            return appServer;
        }

        this.update = (id, receivedAttrs, models)=>{
            return new Promise((resolve, reject) => {

                validateUpdateAttrs(receivedAttrs)
                .then(function(attrs) {
                    var include = getInclude(models);
                    var findServer = DaoService.findById(id, models.app_server, include);
                    return Promise.all([findServer, attrs]);
                })
                .then( function([appServer, attrs]) {
                    validateOptimisticLock(appServer, attrs);
                    appServer = updateAttrsForServer(appServer, attrs);
                    return DaoService.update(appServer);
                })
                .then( function(appServer) {
                    var serverJson = getServerReturnData(appServer);
                    var serverJson = getServerReturnData(appServer);
                    var responseData = {
                        server: serverJson,
                        token: getTokenData(appServer)
                    };
                    resolve(responseData);
                })
                .catch(function(err){
                    reject(err);
                })
            });            
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
        
        this.delete = (id, models)=>{
            return new Promise((resolve, reject) => {
                DaoService.findById(id, models.app_server)
                .then( function(appServer) {
                    return DaoService.delete(appServer);
                })
                .then( function() {
                    resolve();
                })
                .catch(function(err){
                    reject(err);
                })
            });
        }

        function getServerReturnData(appServer){
            var data = appServer.toJSON();
            data['_rev'] = data.rev;
            var user = appServer.User;
            if (user){
                data.createdBy = user.username;
            }
            return _.pick(data, ['id','_rev','createdBy','createdTime','name','lastConnection','host']) ;
        }

        function getTokenData(appServer){
            return {
                token: appServer.token,
                expiresAt: 3600
            };
        }

        this.getById = (id, models) => {
            return new Promise((resolve, reject) => {
                var include = getInclude(models);
                DaoService.findById(id, models.app_server, include)
                .then( function(appServer) {
                    var serverJson = getServerReturnData(appServer);
                    var responseData = {
                        server: serverJson,
                        token: getTokenData(appServer)
                    };
                    resolve(responseData);
                })
                .catch(function(err){
                    reject(err);
                })
            });
        }

        this.get = (models) => {
            return new Promise((resolve, reject) => {
                var include = getInclude(models);
                DaoService.findAll(models.app_server, include)
                .then( function(appServers) {
                    var serversJson = appServers.map(function(appServer) {
                        //return getServerReturnData(appServer);
                        return {
                            server: getServerReturnData(appServer),
                            token: getTokenData(appServer)
                        };
                    });
                    resolve(serversJson);
                })
                .catch(function(err){
                    reject(err);
                })
            });
        }

        function getInclude(models){
            return [{
                model: models.user
            }];
        }
    }
}
module.exports = new ServerService();