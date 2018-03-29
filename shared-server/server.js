'use strict';
/*
 * This is only an express server example
 */
const express = require('express');
var config = require('config');
var routerNode = require('express').Router();
                 require('./app/routes/config.route');
var RouterHandler = require('./app/routes/router')
var bodyParser = require('body-parser');

//Constants
const PORT = process.env.PORT || config.get('server.port');
const HOST = config.get('server.host');

const app = express();

// set middleware parse json
app.use(bodyParser.json({ type: 'application/json' }));

//routes
RouterHandler.loadRoutes(routerNode)
app.use('/api/v1', routerNode);

// App config
app.get('/', (req, res) => {
  res.send('Hello world\n');
});


//start
var server = app.listen(PORT, HOST);
console.log('server atarted and listening on ' + PORT + ':' + HOST);

//to start and stop in tests
module.exports = server;
