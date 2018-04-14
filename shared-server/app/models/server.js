
class Server{
    constructor(data){
        this.id;
        this._rev = data._rev;
        this.createdBy = data.createdBy;
        this.name = data.name;
        this.lastConnection = data.lastConnection;
        this.host = data.host;
        this.port = data.port;
        this.pingUrl = data.pingUrl;
    }
}


module.exports = Server;


