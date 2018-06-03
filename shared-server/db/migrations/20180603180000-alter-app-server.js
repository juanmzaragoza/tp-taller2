'use strict';
module.exports = {
    up: (queryInterface, Sequelize) => {
        return queryInterface.removeColumn('App_servers', 'port')
        .then(function () {
            return queryInterface.removeColumn('App_servers', 'ping');
        })
        then(function(){
            return queryInterface.removeColumn('App_servers', 'stats');
        });
    },
    
    down: (queryInterface, Sequelize) => {
        return queryInterface.addColumn('App_servers', 'port', {
            type: Sequelize.INTEGER,
            allowNull: true
        })
        .then(function(){
            return queryInterface.addColumn('App_servers', 'ping', {
                type: Sequelize.STRING,
                allowNull: true
            });
        })
        .then(function(){
            return queryInterface.addColumn('App_servers', 'stats', {
                type: Sequelize.STRING,
                allowNull: true
            });
        });
    }
};