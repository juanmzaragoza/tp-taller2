var expect = require('chai').expect;
var request = require('request');
var config = require('config');

const PORT = config.get('server.port');
const HOST = config.get('server.host');

console.log(HOST + ':' + PORT);
it('Main page content', function(done) {
	request(`http://${HOST}:${PORT}`, function(error,response,body) {
		expect(body).to.contains('Hello world\n');
		done();
	});
});
