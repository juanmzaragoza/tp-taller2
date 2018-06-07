'use strict';
module.exports = {
    up: (queryInterface, Sequelize) => {
        return queryInterface.createTable('App_servers', {
            id: {
                allowNull: false,
                autoIncrement: true,
                primaryKey: true,
                type: Sequelize.INTEGER
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
                allowNull: false,
                references: {
                    model: 'Users',
                    key: 'id'
                }
            },
            createdTime: {
                allowNull: false,
                type: Sequelize.DATE
            },
            lastConnection: {
                type: Sequelize.DATE,
                allowNull: true
            },
            host: {
                type: Sequelize.STRING,
                allowNull: true
            },
            port: {
                type: Sequelize.INTEGER,
                allowNull: true
            },
            ping: {
                type: Sequelize.STRING,
                allowNull: true
            },
            token: {
                type: Sequelize.STRING,
                allowNull: true
            },
            stats: {
                type: Sequelize.STRING,
                allowNull: true
            }
        });
    },
    down: (queryInterface, Sequelize) => {
        return queryInterface.dropTable('App_servers');
    }
};