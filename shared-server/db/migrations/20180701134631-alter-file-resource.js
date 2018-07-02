'use strict';
module.exports = {
    up: (queryInterface, Sequelize) => {
        return queryInterface.changeColumn('Files', 'resource', {
            type: Sequelize.STRING(1000)
        })
    },

    down: (queryInterface, Sequelize) => {
        
    }
};