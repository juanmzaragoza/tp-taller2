'use strict'
var createError = require('http-errors')
var auth0 = require('jsonwebtoken');
var config = require('../../config/default')

class AuthService {
    constructor(){
        this.token = (usr) => {
            var token = auth0.sign({ data: { username: usr.username, role: usr.role } }, 
            config.auth.tokenSecret, { expiresIn: config.auth.timeExp });
            return token;
        }
        this.requireToken = (req, res, next) =>{
            if(req.headers.authorization){
                var token = req.headers.authorization.split(" ");
                auth0.verify(token[token.length - 1], config.auth.tokenSecret, (error, decoded)=>{
                    if(error){
                        res.status(407).send({ code: -2, message: error.message });
                    }
                    else{
                        return next()
                    }
                })
            }else{
                res.status(401).send({ code: -5, message: 'Unauthorized' });
                
            }
        }

        this.getTokenFromRequest = (req) => {
            var token = req.headers.authorization.split(" ");
            return token[token.length - 1];
        }
    }
}

module.exports = new AuthService();
