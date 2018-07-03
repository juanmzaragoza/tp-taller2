"use strict";
const DaoService    = require('../services/dao.service')
var _               = require("underscore")
const FireBaseService   = require("../services/fire.base.service")

class FileService {
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
            const neededAttrs = ["filename"];
            return validateAttrs(attrs, neededAttrs);
        }

        function validateUpdateAttrs(attrs){
            const neededAttrs = ["filename","rev"];
            return validateAttrs(attrs, neededAttrs);
        }

        function validateUploadAttrs(attrs){
        	const neededAttrs = ["id","rev"];
            return validateAttrs(attrs, neededAttrs);	
        }

        function validateFileData(fileData){
        	return new Promise((resolve, reject) => {
                if (!fileData){
                	reject("invalid-attrs");
                } else {
                    resolve(); 
                }
            }); 
        }

        function validateOptimisticLock(file, attrs){
            if (file.rev != attrs.rev){
                throw "conflict";
            }
        }

        function createFile(attrs, models) {
            attrs.createdTime = Date.now();
            attrs.rev = new Date().getTime();
            return DaoService.insert(models.file, attrs);
        }

        function updateAttrsForFile(file, attrs) {
            var attrsToUpdate = _.pick(attrs, ['filename','size','resource']) ;
            for (var key in attrsToUpdate){
                file[key] = attrsToUpdate[key];
            }
            file.rev = new Date().getTime();
            return file;
        }

        function getFileReturnData(file){
            var data = file.toJSON();
            data['_rev'] = data.rev;
            return _.pick(data, ['id','_rev','createdTime','updatedTime','size','filename','resource']);
        }

        function getResourceUrl(attrs){
            return new Promise((resolve, reject) => {
                if (attrs.hasOwnProperty('resource') && attrs.resource){
                    var fileId = attrs.resource;
                    FireBaseService.getResourceUrl(fileId)
                    .then(function(url){
                        attrs.resource = url;
                        resolve(attrs);
                    })
                    .catch(function(e){
                        resolve(attrs);
                    });
                } else {
                    resolve(attrs);
                }
            });
        }

    	this.add = (attrs, models) => {
            return new Promise((resolve, reject) => {
                validateCreationAttrs(attrs)
                .then( function(attrs) {
                    return getResourceUrl(attrs);
                })
                .then( function(attrs) {
                    return createFile(attrs, models);                    
                })
                .then(function(file){
                    var fileJson = getFileReturnData(file);
                    resolve(fileJson);
                })
                .catch(function(err){
                	reject(err);
                })
            });
        };

        this.update = (id, receivedAttrs, models)=>{
            return new Promise((resolve, reject) => {
                validateUpdateAttrs(receivedAttrs)
                .then(function(attrs) {
                    var file = DaoService.findById(id, models.file);
                    return Promise.all([file, attrs]);
                })
                .then( function([file, attrs]) {
                    validateOptimisticLock(file, attrs);
                    file = updateAttrsForFile(file, attrs);
                    return DaoService.update(file);
                })
                .then( function(file) {
                    var fileJson = getFileReturnData(file);
                    resolve(fileJson);
                })
                .catch(function(err){
                    reject(err);
                })
            });            
        }

        this.uploadFile = (fileData, receivedAttrs, models) => {
        	return new Promise((resolve, reject) => {
        		validateFileData(fileData)
        		.then(function() {
        			return validateUploadAttrs(receivedAttrs);
        		})
        		.then(function(attrs){
        			var file = DaoService.findById(attrs.id, models.file);
                    return Promise.all([file, attrs]);
        		})
        		.then( function([file, attrs]) {
                    validateOptimisticLock(file, attrs);
                    var fileUploadUrl = FireBaseService.upload(fileData)
                	return Promise.all([file, attrs, fileUploadUrl]);
                }).
                then( function([file, attrs, uploadData]){
                	attrs.resource = uploadData.url;
                	attrs.size = uploadData.size;
                    file = updateAttrsForFile(file, attrs);
                    return DaoService.update(file);
                })
                .then( function(file) {
                    var fileJson = getFileReturnData(file);
                    resolve(fileJson);
                })
                .catch(function(err){
                	reject(err);
                })
        	});
        }

        this.get = (models) => {
            return new Promise((resolve, reject) => {
                DaoService.findAll(models.file)
                .then( function(files) {
                    var listJson = files.map(function(file) {
                        return getFileReturnData(file);
                    });
                    resolve(listJson);
                })
                .catch(function(err){
                    reject(err);
                })
            });
        }

        this.getById = (id, models) => {
            return new Promise((resolve, reject) => {
                DaoService.findById(id, models.file)
                .then( function(file) {
                    var responseData = getFileReturnData(file);
                    resolve(responseData);
                })
                .catch(function(err){
                    reject(err);
                })
            });
        }

        this.delete = (id, models)=>{
            return new Promise((resolve, reject) => {
                DaoService.findById(id, models.file)
                .then( function(file) {
                    return DaoService.delete(file);
                })
                .then( function() {
                    resolve();
                })
                .catch(function(err){
                    reject(err);
                })
            });
        }
    }
}

module.exports = new FileService();