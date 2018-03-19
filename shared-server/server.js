'use strict';
/*
 * This is only an express server example
 * Ask for Loopback.js
 *
 */
const express = require('express');
var config = require('config');

//Constants
const PORT = config.get('server.port');
const HOST = config.get('server.host');

// App
const app = express();
app.get('/', (req, res) => {
  res.send('Hello world\n');
});

app.listen(PORT, HOST);
console.log(`Running on http://${HOST}:${PORT}`);