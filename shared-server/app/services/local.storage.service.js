"use strict";
const config = require('../../config/default')

class LocalStorageService {
    constructor() {
        this.load = (username, cb) =>{
            var users = config.server.Users;
            var usr = users.find(user => user["username"] == username)
            cb(usr);
        }
    }
}
module.exports = new LocalStorageService();