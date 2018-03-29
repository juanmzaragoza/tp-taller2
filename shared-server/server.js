'use strict';
/*
 * This is only an express server example
 */
const express = require('express');
var config = require('config');
var routes = require('./app/routes/auth.route');
var bodyParser = require('body-parser');

//Constants
const PORT = process.env.PORT || config.get('server.port');
const HOST = config.get('server.host');

const app = express();

// set middleware 
app.use(bodyParser.json({ type: 'application/json' }));

// App config
app.get('/', (req, res) => {
  res.send('Hello world\n');
});

//routes
app.use(routes);

//start
var server = app.listen(PORT, HOST);
console.log('server atarted and listening on ' + PORT + ':' + HOST);

//to start and stop in tests
module.exports = server;
