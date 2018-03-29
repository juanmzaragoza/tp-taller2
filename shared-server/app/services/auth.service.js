'use strict'
var createError = require('http-errors')
var auth0 = require('jsonwebtoken');
var config = require('../../config/default')

class AuthService {
    constructor(){
        this.token = (username) => {
            var token = auth0.sign({ data: { username: username, role: 'default' } }, 
            config.auth.tokenSecret, { expiresIn: config.auth.timeExp });
            return token;
        }
        this.requireToken = (req, res, next) =>{
            if(req.headers.authorization){
                var token = req.headers.authorization.split(" ");
                auth0.verify(token[token.length - 1], config.auth.tokenSecret, (error, decoded)=>{
                    return next(error)
                })
            }else{
                return next(new createError.Unauthorized());
            }
        }
    }
}

module.exports = new AuthService();
