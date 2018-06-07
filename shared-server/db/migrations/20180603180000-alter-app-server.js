'use strict';
module.exports = {
    up: (queryInterface, Sequelize) => {
        return queryInterface.removeColumn('App_servers', 'port')
        .then(function () {
            return queryInterface.removeColumn('App_servers', 'ping');
        })
        .then(function(){
            return queryInterface.removeColumn('App_servers', 'stats');
        })
        .then(function() {
            return queryInterface.renameColumn('App_servers', 'createdBy', 'userId');
        })
        .then(function(){
            queryInterface.addConstraint('App_servers', ['userId'], {
                type: 'foreign key',
                name: 'app_server_user_fkey',
                references: { //Required field
                    table: 'Users',
                    field: 'id'
                },
                onDelete: 'cascade',
                onUpdate: 'cascade'
            });
        })
        .then(function() {
            return queryInterface.removeConstraint('App_servers','App_servers_createdBy_fkey');
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
        })
        .then(function() {
            return queryInterface.renameColumn('App_servers', 'userId', 'createdBy');
        })
        .then(function() {
            return queryInterface.removeConstraint('App_servers','app_server_user_fkey');
        })
        .then(function(){
            queryInterface.addConstraint('App_servers', ['createdBy'], {
                type: 'foreign key',
                name: 'App_servers_createdBy_fkey',
                references: { //Required field
                    table: 'Users',
                    field: 'id'
                },
                onDelete: 'cascade',
                onUpdate: 'cascade'
            });
        })
        ;
    }
};