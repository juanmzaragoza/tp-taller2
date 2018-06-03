'use strict';
module.exports = (Sequelize, sequelize, models) => {
 	var App_server = sequelize.define('App_server', {
    	id: {
	        type: Sequelize.INTEGER,
	        autoIncrement: true,
	        primaryKey: true
	    },
	    name: {
            type: Sequelize.STRING,
            allowNull: false
        },
  		rev: {
            type: Sequelize.STRING,
            allowNull: true
        },
        createdBy: {
            type: Sequelize.INTEGER,
            allowNull: false
        },
        createdTime: {
        	type: Sequelize.DATE,
        	allowNull: false
        },
        lastConnection: {
        	type: Sequelize.DATE,
        	allowNull: true
        },
        host: {
            type: Sequelize.STRING,
            allowNull: true
        },
        token: {
            type: Sequelize.STRING,
            allowNull: true
        }
	}, {});
	
	App_server.associate = function(models) {
	// associations can be defined here
	};

	models.app_server = App_server;
};