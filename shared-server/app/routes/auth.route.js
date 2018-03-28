'use strict'
var router = require('express').Router();
var loginCont = require('../controllers/login.controller');

router.post('/token', loginCont.token);

module.exports = router;
