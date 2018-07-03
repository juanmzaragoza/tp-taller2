"use strict";
const AuthService   = require('./auth.service')
const bcrypt        = require('bcrypt')
const config        = require('../../config/default')
const DaoService    = require('../services/dao.service')
var _               = require("underscore")

class UserService {
    constructor() {

        function validateAttrs(attrs) {
            if (attrs['_rev']){
                attrs.rev = attrs['_rev'];
                delete attrs['_rev'];
            }
            if (attrs['password']){
                attrs.rawPassword = attrs['password'];
                delete attrs['password'];
            }

            const neededAttrs = [
                //"rev",
                "rawPassword",
                "applicationOwner",
                "username",
                "role"
            ];

            var keys = _.keys(attrs);
            var diff = _.difference(neededAttrs, keys);
            return new Promise((resolve, reject) => {
                if (diff.length == 0){
                    resolve(); 
                } else {
                    reject("invalid-attrs");
                }
            });
        }

        function getHashedPassword(password) {
            return new Promise ( (resolve, reject) => {
                bcrypt.hash(password, config.bcrypt.saltRounds).then(function(hash) {
                    resolve(hash);
                })
                .catch(function(err) {
                    reject("err-hash");
                });
            });
        }

        function createUser(attrs, models) {
            return DaoService.insert(models.user, attrs);
        }

        this.add = (attrs, models) => {
            
            return new Promise((resolve, reject) => {
                validateAttrs(attrs)
                .then( function() {
                    return getHashedPassword(attrs.rawPassword);                    
                })
                .then(function(password) {
                    attrs.password = password;
                    attrs.token = AuthService.token(attrs); // creo que el loguin deberia llamar a login.service
                    return createUser(attrs, models);
                })
                .then(function(user){
                    var responseData = user.toJSON();
                    delete responseData.password;
                    resolve(responseData);
                })
                .catch(function(err){
                    reject(err);
                })
            });
        };


        this.getById = (id, models) => {
            return new Promise((resolve, reject) => {
                DaoService.findById(id, models.user)
                .then( function(user) {
                    var userResponse = user.toJSON();
                    delete userResponse.password;
                    resolve(userResponse);
                })
                .catch(function(err){
                    reject(err);
                })
            });
        }

        this.getUserForToken = (token, models) => {
            return new Promise((resolve, reject) => {
                var filter = {"token": token};
                DaoService.findOne(filter, models.user)
                .then( function(user) {
                    resolve(user);
                })
                .catch(function(err){
                    reject(err);
                })
            });
        }
    }
}
module.exports = new UserService();