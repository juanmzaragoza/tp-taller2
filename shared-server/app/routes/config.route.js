'use strict'
var RouterHandler = require('../routes/router');

var FileCtrl    = require('../controllers/file.controller');
var LoginCont   = require('../controllers/login.controller');
var UserCtrl    = require('../controllers/user.controller');
var ServerCtrl  = require('../controllers/server.controller');
var AppCtrl     = require('../controllers/app.controller')

RouterHandler.addEndpointWithOutAuth({
    verb: 'post',
    path: '/token',
    handler: LoginCont.token
});
RouterHandler.addEndpointWithOutAuth({
    verb: 'post',
    path: '/user',
    handler: UserCtrl.user
});


RouterHandler.addEndpointWithAuth({
    verb: 'get',
    path: '/stats/:id',
    handler: AppCtrl.stats
});
RouterHandler.addEndpointWithAuth({
    verb: 'get',
    path: '/ping/:id',
    handler: AppCtrl.ping
});

RouterHandler.addEndpointWithOutAuth({
    verb: 'get',
    path: '/user/:id',
    handler: UserCtrl.getById
});

RouterHandler.addEndpointWithAuth({
    verb: 'get',
    path: '/files',
    handler: FileCtrl.get
});

RouterHandler.addEndpointWithAuth({
    verb: 'post',
    path: '/files/upload',
    handler: FileCtrl.postUpload
});

RouterHandler.addEndpointWithAuth({
    verb: 'post',
    path: '/servers',
    handler: ServerCtrl.post
});

RouterHandler.addEndpointWithAuth({
    verb: 'post',
    path: '/servers/:id',
    handler: ServerCtrl.refreshToken
});

RouterHandler.addEndpointWithAuth({
    verb: 'get',
    path: '/servers',
    handler: ServerCtrl.get
});

RouterHandler.addEndpointWithAuth({
    verb: 'get',
    path: '/servers/:id',
    handler: ServerCtrl.getById
});

RouterHandler.addEndpointWithAuth({
    verb: 'put',
    path: '/servers/:id',
    handler: ServerCtrl.put
});

RouterHandler.addEndpointWithAuth({
    verb: 'delete',
    path: '/servers/:id',
    handler: ServerCtrl.delete
});
