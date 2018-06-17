/*
// server.js
const path = require('path');
const express = require('express');
const app = express();

// If an incoming request uses
// a protocol other than HTTPS,
// redirect that request to the
// same url but with HTTPS
const forceSSL = function() {
    return function (req, res, next) {
      if (req.headers['x-forwarded-proto'] !== 'https') {
        return res.redirect(
         ['https://', req.get('Host'), req.url].join('')
        );
      }
      next();
    }
  }
  // Instruct the app
  // to use the forceSSL
  // middleware
  app.use(forceSSL());

// For all GET requests, send back index.html
// so that PathLocationStrategy can be used
app.get('/*', function(req, res) {
        res.sendFile(path.join(__dirname + '/dist/index.html'));
    });

// Run the app by serving the static files
// in the dist directory
//app.use(express.static(__dirname + '/dist'));
// Start the app by listening on the default
// Heroku port
app.use(express.static('public'));
app.listen(process.env.PORT || 8080);
*/

var express = require('express');
var app = express();
var path = require('path');
var argPort = undefined;

app.use(express.static(path.join(__dirname, '/dist')));

app.get('/*', function(req, res) {
  res.

sendFile(path.join(__dirname + '/dist/index.html'));
});

if(process.argv.length > 3 && process.argv[2]=='-p'){
  argPort = process.argv[3];
}
var p = argPort || process.env.PORT
console.info(p)
app.listen(p, function () {
  console.log('App started');
});


