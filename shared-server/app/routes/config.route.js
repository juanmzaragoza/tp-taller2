'use strict'
var RouterHandler = require('../routes/router');

var FileCtrl = require('../controllers/file.controller');
var LoginCont = require('../controllers/login.controller');

RouterHandler.addEndpointWithOutAuth({
    verb: 'post',
    path: '/token',
    handler: LoginCont.token
});

RouterHandler.addEndpointWithAuth({
    verb: 'get',
    path: '/files',
    handler: FileCtrl.get
});

RouterHandler.addEndpointWithAuth({
    verb: 'post',
    path: '/files',
    handler: FileCtrl.post
});
