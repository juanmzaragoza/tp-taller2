var expect = require('chai').expect;
var assert = require('chai').assert;
var request = require('request');
var loginServ   = require("../app/services/login.service");
var SequelizeMock = require('sequelize-mock');

var models;

describe('Login Service Tests', function(){

	before(function() {
		var dbMock = new SequelizeMock();
		var UserMock = dbMock.define('user', {
		}, {
		    instanceMethods: {
		    },
		});	

		UserMock.$queryInterface.$useHandler(function(query, queryOptions, done) {
		    if (query === 'findOne') {
		        if (queryOptions[0].where.username == 'admin') {
		            // Result found, return it
		            return UserMock.build({ 
		            	id: 1,
					    username: 'admin',
					    password: '$2b$10$dtJNzWyXhAQa6PdglSjug.XMlNLuf.58cORvB5UAEQmY8vf38DVHG', //1234
					    token: null,
					    token_expiration: null
				    });
		        } else {
		            // No results
		            return null;
		        }
		    }
		});

		models = {
			user: UserMock
		};
	});

	it ('Invalid user should throw unauthorized', function(done) {
		loginServ.auth('test','1234', models)
		.then((result) => {
			assert(false);
	        done();
	    })
	    .catch((reason) => {
	        assert.equal(reason, 'unauthorized');
	        done();
	    });
	});

	it ('Invalid password should throw unauthorized', function(done) {
		loginServ.auth('admin','qweqwe', models)
		.then((result) => {
			assert(false);
	        done();
	    })
	    .catch((reason) => {
	        assert.equal(reason, 'unauthorized');
	        done();
	    });
	});

	it ('Valid credentials should success', function(done) {
		loginServ.auth('admin','1234', models)
		.then((result) => {
			assert.isTrue(result.hasOwnProperty('id'));
			assert.isTrue(result.hasOwnProperty('token'));
			assert.isTrue(result.hasOwnProperty('expiresAt'));
			done();
	    })
	    .catch((reason) => {
	        assert(false);
	        done();
	    });
	});
});