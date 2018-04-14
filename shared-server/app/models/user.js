
class User{
    constructor(data){
        this.id;
        this._rev = data._rev;
        this.password = data.password;
        this.username = data.username;
        this.token;
        this.role = 'default'
        this.tokenFace
        this.type;
        this.applicationOwner = data.applicationOwner;
    }
}


module.exports = User;


