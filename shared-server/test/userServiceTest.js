var expect = require('chai').expect;
var assert = require('chai').assert;
var request = require('request');
var userService   = require("../app/services/user.service");
var SequelizeMock = require('sequelize-mock');

var models;

describe('User Service Tests', function(){

	before(function() {
		var dbMock = new SequelizeMock();
		var UserMock = dbMock.define('user', {
		}, {
			timestamps: false,
		    instanceMethods: {
		    },
		});	

		models = {
			user: UserMock
		};
	});

	
	it ('Add user should success', function(done) {

		var attrs = {
			rev: "lala",
		  	password: "1234",
		  	applicationOwner: "app1",
		  	username: "dummy",
		  	role: "app"
		};

		userService.add(attrs, models)
		.then((user) => {
			assert.equal(user.username, attrs.username);
			assert.equal(user.rev, attrs.rev);
			assert.equal(user.applicationOwner, attrs.applicationOwner);
			assert.equal(user.type, attrs.type);
			assert.notExists(user.password);
			assert.isNotNull(user.id, 'id should not be null');
			done();
		})
		.catch((reason) => {
			assert(false, "Creation failed");
			done();
		});

	});

	it ('Add user with missing attrs should throw invalid-attrs', function(done) {

		var attrs = {
			rev: "lala",
		  	password: "1234",
		  	applicationOwner: "app1",
		  	username: "dummy"
		};

		userService.add(attrs, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'invalid-attrs');
			done();
		});

	});

});