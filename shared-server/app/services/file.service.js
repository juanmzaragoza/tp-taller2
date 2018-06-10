"use strict";
const DaoService    = require('../services/dao.service')
var _               = require("underscore")

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

        function createFile(attrs, models) {
            attrs.createdTime = Date.now();
            attrs.rev = new Date().getTime();
            return DaoService.insert(models.file, attrs);
        }

        function getFileReturnData(file){
            var data = file.toJSON();
            data['_rev'] = data.rev;
            return _.pick(data, ['id','_rev','createdTime','updatedTime','size','filename','resource']);
        }

    	this.add = (attrs, models) => {
            return new Promise((resolve, reject) => {
                validateCreationAttrs(attrs)
                .then( function() {
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
    }
}

module.exports = new FileService();