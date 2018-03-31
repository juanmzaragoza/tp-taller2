var expect = require('chai').expect;
var assert = require('chai').assert;
var request = require('request');
var config = require('config');

const PORT = config.get('server.port');
const HOST = config.get('server.host');
const PREFIX_PATH = '/api/v1';

describe('endpoints', function(){
	var server;

	before(function() {
		server = require('../server.js');
	  });
  
	  beforeEach(function() {
	  });
  
	  afterEach(function() {
	  });
  
	  after(function() {
		server.close();
	  });
	
	it('loading express: Main page content /', function(done) {
		request(`http://${HOST}:${PORT}`, function(error,response,body) {
			expect(body).to.contains('Hello world\n');
			done();
		});
	});
	it('/token', function(done) {
		request.post({
			url:`http://${HOST}:${PORT}${PREFIX_PATH}`+'/token', 
			formData: '{"username":"Erik","password":"Erik"}'
		}, 
		(err, httpResponse, body) => {
			assert.exists(JSON.parse(body)['token']);
			assert.equal(JSON.parse(body)['token'].length, 163);
			done(err);
		});
	});

	it('/files', function(done) {
		request.post({
			url:`http://${HOST}:${PORT}${PREFIX_PATH}`+'/token', 
			formData: '{"username":"Erik","password":"Erik"}'
		}, 
		(err, httpResponse, body) => {
			var token = JSON.parse(body)['token'];
			request.get({
				url:`http://${HOST}:${PORT}${PREFIX_PATH}`+'/files', 
				headers: { Authorization:"Bearer " + token }
			}, 
			(err, httpResponse, body) => {
				assert.exists(JSON.parse(body)['file']);
				assert.equal(JSON.parse(body)['file'], 'file.jpg');
				done(err);
			});
		});
	});
	
})
