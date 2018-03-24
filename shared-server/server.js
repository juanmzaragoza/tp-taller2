'use strict';
/*
 * This is only an express server example
 */
const express = require('express');
var config = require('config');

//Constants
const PORT = config.get('server.port');
const HOST = config.get('server.host');

const app = express();

// App config
app.get('/', (req, res) => {
  res.send('Hello world\n');
});

//start
var server = app.listen(PORT, HOST);
console.log('server atarted and listening on ' + PORT + ':' + HOST);

//to start and stop in tests
module.exports = server;
