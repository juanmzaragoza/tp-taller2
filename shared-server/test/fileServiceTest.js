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



});