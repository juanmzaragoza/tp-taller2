'use strict';
module.exports = (Sequelize, sequelize, models) => {
    var User = sequelize.define('User', {
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
        rev: {
            type: Sequelize.STRING,
            allowNull: true
        },
        token: {
            type: Sequelize.STRING,
            allowNull: true
        },
        role: {
            type: Sequelize.STRING,
            allowNull: true
        },
        tokenFace: {
            type: Sequelize.STRING,
            allowNull: true
        },
        type: {
            type: Sequelize.STRING,
            allowNull: true
        },
        applicationOwner: {
            type: Sequelize.STRING,
            allowNull: true
        },
    }, {});
    
    User.associate = function(models) {
        // associations can be defined here
    };

    models.user = User;
};