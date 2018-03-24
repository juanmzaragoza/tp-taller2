var expect = require('chai').expect;
var request = require('request');
var config = require('config');

const PORT = config.get('server.port');
const HOST = config.get('server.host');

describe('loading express', function(){
	var server;

	beforeEach(function() {
		server = require('../server.js');
	});

	afterEach(function() {
		server.close();
	});
	
	it('Main page content', function(done) {
		request(`http://${HOST}:${PORT}`, function(error,response,body) {
			expect(body).to.contains('Hello world\n');
			done();
		});
	});
})
