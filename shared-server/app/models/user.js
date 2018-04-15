// class User{
//     constructor(data){
//         this.id;
//         this._rev = data._rev;
//         this.password = data.password;
//         this.username = data.username;
//         this.token;
//         this.tokenFace
//         this.type;
//         this.applicationOwner = data.applicationOwner;
//     }
// }
// module.exports = User;

module.exports = function (Sequelize, sequelize, models) {
    const User = sequelize.define('user', {
        id: {
            type: Sequelize.INTEGER,
            autoIncrement: true,
            primaryKey: true
        },
        username: {
            type: Sequelize.STRING,
            allowNull: false
        },
        password: {
            type: Sequelize.STRING,
            allowNull: false
        },
        token: {
            type: Sequelize.STRING,
            allowNull: true
        },
        token_expiration: {
            type: Sequelize.DATE,
            allowNull: true
        },
    });

    models.user = User;
};


