'use strict';
module.exports = (Sequelize, sequelize, models) => {
  
 	var File = sequelize.define('File', {
 		id: {
            type: Sequelize.INTEGER,
            autoIncrement: true,
            primaryKey: true
        },
        rev: {
            type: Sequelize.STRING,
            allowNull: true
        },
        size: {
	    	type: Sequelize.INTEGER,
	        defaultValue: 0,
	        allowNull: false
	    },
		filename: {
			type: Sequelize.STRING,
			allowNull: true
		},
	    resource: {
	    	type: Sequelize.STRING,
	    	allowNull: true
	    },
	}, {
		timestamps: true,
		createdAt: 'createdTime',
		updatedAt: 'updatedTime',
	});
  
	File.associate = function(models) {
    	// associations can be defined here
  	};
  
  	models.file = File;
};