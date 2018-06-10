var expect = require('chai').expect;
var assert = require('chai').assert;
var request = require('request');
var fileService   = require("../app/services/file.service");
var SequelizeMock = require('sequelize-mock');

var models;

describe('File Service Tests', function(){

	beforeEach(function() {
		var dbMock = new SequelizeMock();
		var FileMock = dbMock.define('File', {
		}, {
			timestamps: true,
			createdAt: 'createdTime',
			updatedAt: 'updatedTime',
		    instanceMethods: {
		    },
		});	

		models = {
			file: FileMock
		};
	});

	it ('Add file with missing attrs should throw invalid-attrs', function(done) {
		var attrs = {};
		fileService.add(attrs, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'invalid-attrs');
			done();
		});
	});

	it ('Add file with missing attrs should throw invalid-attrs 2', function(done) {
		var attrs = {
			'_rev': '123123123'
		};
		fileService.add(attrs, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'invalid-attrs');
			done();
		});
	});

	it ('Add file should success', function(done) {
		var attrs = {
			filename: "dummy"
		};
		fileService.add(attrs, models)
		.then((response) => {
			assert.notEqual(response,null);
			assert.notEqual(response.id,null);
			assert.equal(response.filename,'dummy');
			done();
		})
		.catch((reason) => {
			assert(false, "Creation failed: "+reason);
			done();
		});
	});

	it ('Update file with missing attrs should throw invalid-attrs', function(done) {
		var attrs = {
			'rev': '123123123'
		};
		fileService.update(14, attrs, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'invalid-attrs');
			done();
		});
	});

	it ('Update inexistent file should throw not-found', function(done) {
		models.file.$queryInterface.$useHandler(function(query, queryOptions, done) {
		    if (query === 'findById') {
		    	if (queryOptions[0] === 14) {
		            return null
		        }
		    }
		});
		var attrs = {
			'rev': '123123123',
			'filename': 'lalal'
		};
		fileService.update(14, attrs, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'not-found');
			done();
		});
	});

	it ('Update file with invalid rev should throw conflict', function(done) {
		models.file.$queryInterface.$useHandler(function(query, queryOptions, done) {
		    if (query === 'findById') {
		    	if (queryOptions[0] === 12) {
		    		return models.file.build({ 
		            	"id": 12,
		            	"rev": "456",
						"createdTime": "2018-05-26T17:19:51.342Z",
						"updatedTime": "2018-05-26T17:19:51.342Z",
						"filename": "dummy"
				    });
		        } else {
		        	return null;
		        }
		    }
		});
		var attrs = {
			'rev': '123',
			'filename': 'lalal'
		};
		fileService.update(12, attrs, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'conflict');
			done();
		});
	});

	it ('Update file should success', function(done) {
		models.file.$queryInterface.$useHandler(function(query, queryOptions, done) {
			if (query === 'findById') {
		    	if (queryOptions[0] == 5) {
		    		return models.file.build({ 
		            	"id": 5,
		            	"rev": "456",
						"createdTime": "2018-05-26T17:19:51.342Z",
						"updatedTime": "2018-05-26T17:19:51.342Z",
						"filename": "dummy",
						"size": 0,
						"resource": null
				    });
		        } else {
		        	return null;
		        }
		    }
		});
		var attrs = {
			filename: "new name",
			rev: "456"
		};
		fileService.update(5, attrs, models)
		.then((response) => {
			assert.notEqual(response,null);
			assert.equal(response.id,5);
			assert.equal(response.filename,'new name');
			assert.notEqual(response._rev,"456");
			done();
		})
		.catch((reason) => {
			assert(false, "Update failed: "+reason);
			done();
		});
	});


});