var expect = require('chai').expect;
var assert = require('chai').assert;
var authService   = require("../app/services/auth.service");

describe('Auth Service Tests', function(){

	before(function() {
		
	});

	it ('getTokenFromRequest', function(done) {
		var req = {
			headers:{
				authorization: "Basic token1"
			}
		};
		var token = authService.getTokenFromRequest(req);
		assert.equal(token, 'token1');
		done();
	});

	it ('getTokenFromRequest 2', function(done) {
		var req = {
			headers:{
				authorization: "Basic token2"
			}
		};
		var token = authService.getTokenFromRequest(req);
		assert.equal(token, 'token2');
		done();
	});

});