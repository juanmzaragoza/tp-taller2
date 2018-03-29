'use strict'
var AuthService = require('../services/auth.service');

class RouterHandler{
    constructor(){
        this.endpointWithAuth = [];
        this.endpointWithOutAuth = [];

        this.addEndpointWithAuth = (endpoint) =>{
            this.endpointWithAuth.push(endpoint);
        }
        this.addEndpointWithOutAuth = (endpoint) =>{
            this.endpointWithOutAuth.push(endpoint);
        }
        this.loadRoutes = (routerNode) => {
            this.endpointWithAuth.forEach(endpoint => {
                routerNode[endpoint.verb](endpoint.path, AuthService.requireToken, endpoint.handler);
            });
            this.endpointWithOutAuth.forEach(endpoint => {
                routerNode[endpoint.verb](endpoint.path, endpoint.handler);
            });
        }
    }
}

module.exports = new RouterHandler();
