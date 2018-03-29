'use strict'
var auth0 = require('jsonwebtoken');
var config = require('../../config/default')

class AuthService {
    constructor(){
        this.token = (username) => {
            var token = auth0.sign({ data: { username: username, role: 'default' } }, 
            config.auth.tokenSecret, { expiresIn: config.auth.timeExp });
            return token;
        }
    }
}

module.exports = new AuthService();
