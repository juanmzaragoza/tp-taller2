"use strict";
const AuthService       = require('./auth.service')
const DaoService    = require('../services/dao.service')
const RemoteServ  = require('../services/remote.service')
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
            var attrsToUpdate = _.pick(attrs, ['name','host']) ;
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

        function updateLastConnection(appServer) {
            appServer.lastConnection = Date.now();
            return DaoService.update(appServer);
        }

        this.updateLastConnection = function(apiKey, models){
            return new Promise((resolve, reject) => {
                if (apiKey){
                    var filter = {"token": apiKey};
                    DaoService.findOne(filter, models.app_server)
                    .then( function(appServer) {
                        return updateLastConnection(appServer);
                    })
                    .then(function(){
                        resolve();
                    })
                    .catch(function(err){
                        resolve();
                    })
                } else {
                    resolve();
                }
            });
        }
        this.ping = (id, models)=>{
            return new Promise((resolve, reject)=>{
                var filter = {"id": id};
                DaoService.findOne(filter, models.app_server)
                .then( function(server) {
                    var url = server.host + '/ping'
                    console.log(url)
                    RemoteServ.get(url)
                    .then(res=>{
                        resolve(res.server)
                    })
                    .catch(reject)
                })
                .catch(reject)
            })
        }
        this.stats = (id, models)=>{
            return new Promise((resolve, reject)=>{
                var filter = {"id": id};
                DaoService.findOne(filter, models.app_server)
                .then( function(server) {
                    var url = server.host + '/stats'
                    console.log(url)
                    RemoteServ.get(url)
                    .then(res=>{
                        resolve(res.stats)
                    })
                    .catch(reject)
                })
                .catch(reject)
            })
        }
        this.requests = (id, args, models)=>{
            return new Promise((resolve, reject)=>{
                var filter = {"id": id};
                DaoService.findOne(filter, models.app_server)
                .then( function(server) {
                    var url = server.host + '/requests'
                    if (args.from && args.to){
                        url += '?from=' + args.from + '&to=' + args.to;
                    } else if (args.from) {
                        url += '?from=' + args.from;
                    } else if (args.to) {
                        url += '?to=' + args.to;
                    }
                    RemoteServ.get(url)
                    .then(res=>{
                        resolve(res)
                    })
                    .catch(reject)
                })
                .catch(reject)
            })
        }
    }
}
module.exports = new ServerService();