var expect = require('chai').expect;
var assert = require('chai').assert;
var request = require('request');
var config = require('config');

const PORT = config.get('server.port');
const HOST = config.get('server.host');
const PREFIX_PATH = '/api';

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
	it('/user', function(done) {
		var data = {
			"id": "string",
			"_rev": "string",
			"password": "clave",
			"applicationOwner": "app1",
			"username": "erik"
		};
		request.post({url:`http://${HOST}:${PORT}${PREFIX_PATH}`+'/user', 
		form: data}, 
		function(err,httpResponse,body){
			assert.exists(JSON.parse(body)['user']['id']);
			assert.equal(JSON.parse(body)['user']['id'].length, 36);
			done(err);
		}) 
	});
	
	it('/token', function(done) {
		var data = {
			"id": "string",
			"_rev": "string",
			"password": "clave",
			"applicationOwner": "app1",
			"username": "erik"
		};
		request.post({url:`http://${HOST}:${PORT}${PREFIX_PATH}`+'/user', 
		form: data}, 
		function(err,httpResponse,body){
			var data2 = {
				"password": "clave",
				"username": "erik"
			}
			request.post({url:`http://${HOST}:${PORT}${PREFIX_PATH}`+'/token', 
			form: data2}, 
			function(err,httpResponse,body){
				assert.exists(JSON.parse(body)['token']['token']);
				assert.equal(JSON.parse(body)['token']['token'].length, 417);
				done(err);
			}) 
		})
		
	});

	it('/files', function(done) {
		var data = {
			"id": "string",
			"_rev": "string",
			"password": "clave",
			"applicationOwner": "app1",
			"username": "erik"
		};
		request.post({url:`http://${HOST}:${PORT}${PREFIX_PATH}`+'/user', 
		form: data}, 
		function(err,httpResponse,body){
			var data2 = {
				"password": "clave",
				"username": "erik"
			}
			request.post({url:`http://${HOST}:${PORT}${PREFIX_PATH}`+'/token', 
			form: data2}, 
			function(err,httpResponse,body){
				var token = JSON.parse(body)['token']['token'];
				request.get({
					url:`http://${HOST}:${PORT}${PREFIX_PATH}`+'/files', 
					headers: { Authorization:"Bearer " + token }
				}, 
				(err, httpResponse, body) => {
					assert.exists(JSON.parse(body)['servers']);
					assert.equal(JSON.parse(body)['servers'][0].filename, 'file.jpg');
					done(err);
				});
			}) 
		})
	});
	it('/user/:id', function(done) {
		var data = {
			"id": "string",
			"_rev": "string",
			"password": "clave",
			"applicationOwner": "app1",
			"username": "erik"
		};
		request.post({url:`http://${HOST}:${PORT}${PREFIX_PATH}`+'/user', 
		form: data}, 
		function(err,httpResponse,res){
			let id = JSON.parse(res).user.id
			var data2 = {
				"password": "clave",
				"username": "erik"
			}
			request.post({url:`http://${HOST}:${PORT}${PREFIX_PATH}`+'/token', 
			form: data2}, 
			function(err,httpResponse,body){
				var token = JSON.parse(body)['token']['token'];
				request.get({
					url:`http://${HOST}:${PORT}${PREFIX_PATH}`+'/user/'+id, 
					headers: { Authorization:"Bearer " + token }
				}, 
				(err, httpResponse, body) => {
					assert.equal(JSON.parse(body)['user'].username, 'erik');
					done(err);
				});
			}) 
		})
	});
	
})
