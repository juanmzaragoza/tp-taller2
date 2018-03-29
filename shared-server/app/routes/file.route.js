'use strict'
var router = require('express').Router();
var fileCtrl = require('../controllers/file.controller');
var AuthService = require('../services/auth.service') 

router.get('/files', AuthService.requireToken, fileCtrl.get);

module.exports = router;
