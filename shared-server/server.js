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
app.use(bodyParser.urlencoded({ extended: true })); 

//user-defined middleware
app.use((req, res, next) => {
  res.append('Access-Control-Allow-Origin', ['*']);
  res.append('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
  res.append('Access-Control-Allow-Headers', 'Content-Type');
  next();
});

//routes
RouterHandler.loadRoutes(routerNode)
app.use('/api', routerNode);

// App config
app.get('/', (req, res) => {
  res.send('Hello world\n');
});

// Database
const Sequelize = require('sequelize');
const sequelize = new Sequelize(
	config.get('database.database'),
	config.get('database.username'),
	config.get('database.password'),
	{
		host: config.get('database.host'),
		port : config.get('database.port'),
		dialect: config.get('database.dialect'),
		operatorsAliases: false,
		pool: {
			max: 5,
			min: 0,
			acquire: 30000,
			idle: 10000
		},
	});

sequelize
	.authenticate()
	.then(() => {
		console.log('Connection has been established successfully.');


		sequelize.query("SELECT * FROM prueba", { type: sequelize.QueryTypes.SELECT})
		.then(prueba => {
			console.log("prueba:", prueba);
		})
	})
	.catch(err => {
		console.error('Unable to connect to the database:', err);
	});


//start
var server = app.listen(PORT, HOST);
console.log('server started and listening on ' + PORT + ':' + HOST);

//to start and stop in tests
module.exports = server;
