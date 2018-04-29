'use strict';
module.exports = (sequelize, DataTypes) => {
    var User = sequelize.define('User', {
        id: {
            type: DataTypes.INTEGER,
            autoIncrement: true,
            primaryKey: true
        },
        username: {
            type: DataTypes.STRING,
            allowNull: false
        },
        password: {
            type: DataTypes.STRING,
            allowNull: false
        },
        rev: {
            type: DataTypes.STRING,
            allowNull: true
        },
        token: {
            type: DataTypes.STRING,
            allowNull: true
        },
        role: {
            type: DataTypes.STRING,
            allowNull: true
        },
        tokenFace: {
            type: DataTypes.STRING,
            allowNull: true
        },
        type: {
            type: DataTypes.STRING,
            allowNull: true
        },
        applicationOwner: {
            type: DataTypes.STRING,
            allowNull: true
        },
    }, {});
    
    User.associate = function(models) {
        // associations can be defined here
    };

    return User;
};