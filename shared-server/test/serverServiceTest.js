var expect = require('chai').expect;
var assert = require('chai').assert;
var request = require('request');
var serverService   = require("../app/services/server.service");
var SequelizeMock = require('sequelize-mock');

var models;

describe('Server Service Tests', function(){

	beforeEach(function() {
		var dbMock = new SequelizeMock();
		var ServerMock = dbMock.define('App_server', {
		}, {
			timestamps: false,
		    instanceMethods: {
		    },
		});	

		var UserMock = dbMock.define('User', {
			id: 5,
			username: 'admin'
		}, {
			timestamps: false
		});

		models = {
			app_server: ServerMock,
			user: UserMock
		};
	});

	it ('Add server with missing attrs should throw invalid-attrs', function(done) {
		var user = models.user.build();
		var attrs = {};
		serverService.add(attrs, models, user)
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
		var user = models.user.build();
		var attrs = {
			'_rev': '123123123'
		};
		serverService.add(attrs, models, user)
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
		var user = models.user.build();
		var attrs = {
			name: "dummy"
		};
		serverService.add(attrs, models, user)
		.then((response) => {
			assert.isNotNull(response, 'response should not be null');
			assert.notEqual(response,null);
			assert.notEqual(response.server,null);
			assert.notEqual(response.server._rev,null);
			assert.notEqual(response.server.createdBy,null);
			assert.notEqual(response.server.createdTime,null);
			assert.notEqual(response.server.name);
			assert.equal(response.server.name,'dummy');
			assert.equal(response.server.createdBy,'admin');
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

	it ('Add server should success 2', function(done) {
		var user = models.user.build();
		var attrs = {
			name: "dummy 2",
			createdBy: 7
		};
		serverService.add(attrs, models, user)
		.then((response) => {
			assert.isNotNull(response, 'response should not be null');
			assert.notEqual(response,null);
			assert.notEqual(response.server,null);
			assert.notEqual(response.server._rev,null);
			assert.notEqual(response.server.createdTime,null);
			assert.equal(response.server.name,'dummy 2');
			assert.equal(response.server.createdBy,'admin');
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

	it ('Update inexistent server should throw not-found', function(done) {
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
		            	"rev": "456",
						"createdBy": 5,
						"createdTime": "2018-05-26T17:19:51.342Z",
						"name": "dummy",
						"lastConnection": null
				    });
		        } else {
		        	return null;
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

	it ('Update server should success', function(done) {
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
			if (query === 'findById') {
		    	if (queryOptions[0] == 5) {
		    		var appServer = models.app_server.build({ 
		            	"id": 5,
		            	"rev": "456",
						"createdTime": "2018-05-26T17:19:51.342Z",
						"name": "dummy",
						"lastConnection": null,
						"userId": 5,
						"token": "es un token"
				    });
				    appServer.User = models.user.build();
				    return appServer;
		        } else {
		        	return null;
		        }
		    }
		});
		var attrs = {
			name: "new name",
			rev: "456"
		};
		serverService.update(5, attrs, models)
		.then((response) => {
			assert.notEqual(response,null);
			assert.notEqual(response.server.createdTime,null);
			assert.equal(response.server.id,5);
			assert.equal(response.server.name,'new name');
			assert.equal(response.server.createdBy,"admin");
			assert.notEqual(response.server._rev,"456");
			assert.notEqual(response.token,null);
			assert.equal(response.token.token,"es un token");
			assert.notEqual(response.token.expiresAt,null);
			done();
		})
		.catch((reason) => {
			assert(false, "Update failed: "+reason);
			done();
		});
	});

	it ('Update server should success 2', function(done) {
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
			if (query === 'findById') {
		    	if (queryOptions[0] == 8) {
		            var appServer = models.app_server.build({ 
		            	"id": 8,
		            	"rev": "123",
						"createdTime": "2018-05-26T17:19:51.342Z",
						"name": "dummy",
						"lastConnection": null,
						"token": "es un token"
				    });
				    appServer.User = models.user.build();
				    return appServer;
		        } else {
		        	return null;
		        }
		    }
		});
		var attrs = {
			name: "new name 2",
			rev: "123"
		};
		serverService.update(8, attrs, models)
		.then((response) => {
			assert.notEqual(response,null);
			assert.equal(response.server.createdTime,"2018-05-26T17:19:51.342Z");
			assert.equal(response.server.id,8);
			assert.equal(response.server.name,'new name 2');
			assert.equal(response.server.createdBy,"admin");
			assert.notEqual(response._rev,"123");
			assert.notEqual(response.token,null);
			assert.equal(response.token.token,"es un token");
			assert.notEqual(response.token.expiresAt,null);
			done();
		})
		.catch((reason) => {
			assert(false, "Update failed: "+reason);
			done();
		});
	});

	it ('getById inexistent server should throw not-found', function(done) {
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
		    if (query === 'findById') {
		    	if (queryOptions[0] === 14) {
		            return null
		        }
		    }
		});
		serverService.getById(14, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'not-found');
			done();
		});
	});

	it ('getById server should success', function(done) {
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
			if (query === 'findById') {
		    	if (queryOptions[0] == 8) {
		            var appServer = models.app_server.build({ 
		            	"id": 8,
		            	"rev": "123",
						"createdTime": "2018-05-26T17:19:51.342Z",
						"name": "dummy",
						"lastConnection": null,
						"token": "es un token"
				    });
				    appServer.User = models.user.build();
				    return appServer;
		        } else {
		        	return null;
		        }
		    }
		});
		serverService.getById(8, models)
		.then((response) => {
			assert.notEqual(response,null);
			assert.equal(response.server.createdTime,"2018-05-26T17:19:51.342Z");
			assert.equal(response.server.id,8);
			assert.equal(response.server.name,'dummy');
			assert.equal(response.server.createdBy,"admin");
			assert.equal(response.server._rev,"123");
			assert.notEqual(response.token,null);
			assert.equal(response.token.token,"es un token");
			assert.notEqual(response.token.expiresAt,null);
			done();
		})
		.catch((reason) => {
			assert(false, "getById failed: "+reason);
			done();
		});
	});

	it ('getById server should success 2', function(done) {
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
			if (query === 'findById') {
		    	if (queryOptions[0] == 87) {
		            var appServer = models.app_server.build({ 
		            	"id": 87,
		            	"rev": "123",
						"createdTime": "2018-05-26T17:19:51.342Z",
						"name": "otro dummy",
						"lastConnection": null,
						"token": "es un token"
				    });
				    appServer.User = models.user.build();
				    return appServer;
		        } else {
		        	return null;
		        }
		    }
		});
		serverService.getById(87, models)
		.then((response) => {
			assert.notEqual(response,null);
			assert.equal(response.server.createdTime,"2018-05-26T17:19:51.342Z");
			assert.equal(response.server.id,87);
			assert.equal(response.server.name,'otro dummy');
			assert.equal(response.server.createdBy,"admin");
			assert.equal(response.server._rev,"123");
			assert.notEqual(response.token,null);
			assert.equal(response.token.token,"es un token");
			assert.notEqual(response.token.expiresAt,null);
			done();
		})
		.catch((reason) => {
			assert(false, "getById failed: "+reason);
			done();
		});
	});

	it ('get server should return empty list', function(done) {
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
			if (query === 'findAll') {
		    	return [];
		    }
		});
		serverService.get(models)
		.then((response) => {
			assert.deepEqual(response,[]);
			done();
		})
		.catch((reason) => {
			assert(false, "get failed: "+reason);
			done();
		});
	});

	it ('get server should return list', function(done) {
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
			if (query === 'findAll') {
				var server1 = models.app_server.build({ 
	            	"id": 87,
	            	"rev": "123",
					"createdTime": "2018-05-26T17:19:51.342Z",
					"name": "otro dummy",
					"lastConnection": null,
					"token": "token 1"
			    });
			    server1.User = models.user.build();
			    var server2 = models.app_server.build({ 
	            	"id": 8,
	            	"rev": "123",
					"createdTime": "2018-05-26T17:19:51.342Z",
					"name": "dummy",
					"lastConnection": null,
					"token": "token 2"
			    });
			    server2.User = models.user.build();
		    	return [server1, server2];
		    }
		});
		serverService.get(models)
		.then((response) => {
			assert.deepEqual(response,[
				{
					server:	{
						"id": 87,
		            	"_rev": "123",
						"createdBy": "admin",
						"createdTime": "2018-05-26T17:19:51.342Z",
						"name": "otro dummy",
						"lastConnection": null
					},
					token: {
						token: "token 1",
						expiresAt: 3600
					}
				},
				{
					server: {			
		            	"id": 8,
		            	"_rev": "123",
						"createdBy": "admin",
						"createdTime": "2018-05-26T17:19:51.342Z",
						"name": "dummy",
						"lastConnection": null,
					},
					token: {
						token: "token 2",
						expiresAt: 3600
					}
			    }
			]);
			done();
		})
		.catch((reason) => {
			assert(false, "get failed: "+reason);
			done();
		});
	});

	it ('delete inexistent server should throw not-found', function(done) {
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
		    if (query === 'findById') {
		    	if (queryOptions[0] === 14) {
		            return null
		        }
		    }
		});
		serverService.delete(14, models)
		.then((result) => {
			assert(false);
			done();
		})
		.catch((reason) => {
			assert.equal(reason, 'not-found');
			done();
		});
	});

	it ('delete server should success', function(done) {
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
			if (query === 'findById') {
		    	if (queryOptions[0] == 8) {
		            return models.app_server.build({ 
		            	"id": 8,
		            	"rev": "123",
						"createdBy": 4,
						"createdTime": "2018-05-26T17:19:51.342Z",
						"name": "dummy",
						"lastConnection": null
				    });
		        } else {
		        	return null;
		        }
		    }
		});
		serverService.delete(8, models)
		.then((response) => {
			assert(true);
			done();
		})
		.catch((reason) => {
			assert(false, "getById failed: "+reason);
			done();
		});
	});

	it ('Update last connection', function(done) {
		var apiKey = '12345';
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
			if (query === 'findOne') {
		    	if (queryOptions[0] == apiKey) {
		            return models.app_server.build({ 
		            	"id": 8,
		            	"rev": "123",
						"createdBy": 4,
						"createdTime": "2018-05-26T17:19:51.342Z",
						"name": "dummy",
						"lastConnection": null
				    });
		        } else {
		        	return null;
		        }
		    }
		});

		serverService.updateLastConnection(apiKey, models)
		.then((response) => {
			assert(true);
			done();
		})
		.catch((reason) => {
			assert(false, "getById failed: "+reason);
			done();
		});
	});

	it ('Update last connection without server should fail silently', function(done) {
		var apiKey = '12345';
		models.app_server.$queryInterface.$useHandler(function(query, queryOptions, done) {
			if (query === 'findOne') {
		    	return null;
		    }
		});

		serverService.updateLastConnection(apiKey, models)
		.then((response) => {
			assert(true);
			done();
		})
		.catch((reason) => {
			assert(false, "getById failed: "+reason);
			done();
		});
	});

});