var expect = require('chai').expect;
var assert = require('chai').assert;
var request = require('request');
var serverService   = require("../app/services/server.service");
var SequelizeMock = require('sequelize-mock');

var models;

describe('Server Service Tests', function(){

	before(function() {
		var dbMock = new SequelizeMock();
		var ServerMock = dbMock.define('App_server', {
		}, {
			timestamps: false,
		    instanceMethods: {
		    },
		});	

		models = {
			app_server: ServerMock
		};
	});

	it ('Add server with missing attrs should throw invalid-attrs', function(done) {
		var attrs = {};
		serverService.add(attrs, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'invalid-attrs');
			done();
		});
	});

	it ('Add server with missing attrs should throw invalid-attrs 2', function(done) {
		var attrs = {
			'_rev': '123123123'
		};
		serverService.add(attrs, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'invalid-attrs');
			done();
		});
	});

	
	it ('Add server should success', function(done) {
		var attrs = {
			name: "dummy",
			createdBy: 4
		};
		serverService.add(attrs, models)
		.then((response) => {
			assert.isNotNull(response, 'response should not be null');
			assert.notEqual(response,null);
			assert.notEqual(response.server,null);
			assert.notEqual(response.server._rev,null);
			assert.notEqual(response.server.createdBy,null);
			assert.notEqual(response.server.createdTime,null);
			assert.notEqual(response.server.name);
			assert.equal(response.server.name,'dummy');
			assert.equal(response.server.createdBy,4);
			assert.notEqual(response.token,null);
			assert.notEqual(response.token,null);
			assert.notEqual(response.token.token,null);
			assert.notEqual(response.token.expiresAt,null);
			done();
		})
		.catch((reason) => {
			console.log(reason);
			assert(false, "Creation failed: "+reason);
			done();
		});
	});

	it ('Add server should success', function(done) {
		var attrs = {
			name: "dummy 2",
			createdBy: 7
		};
		serverService.add(attrs, models)
		.then((response) => {
			assert.isNotNull(response, 'response should not be null');
			assert.notEqual(response,null);
			assert.notEqual(response.server,null);
			assert.notEqual(response.server._rev,null);
			assert.notEqual(response.server.createdBy,null);
			assert.notEqual(response.server.createdTime,null);
			assert.notEqual(response.server.name);
			assert.equal(response.server.name,'dummy 2');
			assert.equal(response.server.createdBy,7);
			assert.notEqual(response.token,null);
			assert.notEqual(response.token,null);
			assert.notEqual(response.token.token,null);
			assert.notEqual(response.token.expiresAt,null);
			done();
		})
		.catch((reason) => {
			console.log(reason);
			assert(false, "Creation failed: "+reason);
			done();
		});
	});

	it ('Update server with missing attrs should throw invalid-attrs', function(done) {
		var attrs = {
			'rev': '123123123'
		};
		serverService.update(14, attrs, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'invalid-attrs');
			done();
		});
	});

	it ('Update inexisten server should throw not-found', function(done) {
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
		    if (query === 'findById') {
		    	if (queryOptions[0] === 14) {
		            // Result found, return it
		            return null
		        }
		    }
		});

		var attrs = {
			'rev': '123123123',
			'name': 'lalal'
		};
		serverService.update(14, attrs, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'not-found');
			done();
		});
	});

	it ('Update inexistenT server should throw not-found', function(done) {
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
		    if (query === 'findById') {
		    	if (queryOptions[0] === 14) {
		            return null
		        }
		    }
		});
		var attrs = {
			'rev': '123123123',
			'name': 'lalal'
		};
		serverService.update(14, attrs, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'not-found');
			done();
		});
	});

	it ('Update server with invalid rev should throw conflict', function(done) {
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
		    if (query === 'findById') {
		    	if (queryOptions[0] === 12) {
		            return models.app_server.build({ 
		            	"id": 12,
		            	"_rev": "456",
						"createdBy": 5,
						"createdTime": "2018-05-26T17:19:51.342Z",
						"name": "dummy",
						"lastConnection": null
				    });
		        }
		    }
		});
		var attrs = {
			'rev': '123',
			'name': 'lalal'
		};
		serverService.update(12, attrs, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'conflict');
			done();
		});
	});

	

});