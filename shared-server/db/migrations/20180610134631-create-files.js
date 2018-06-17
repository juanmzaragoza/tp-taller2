'use strict';
module.exports = {
  up: (queryInterface, Sequelize) => {
    return queryInterface.createTable('Files', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      rev: {
        type: Sequelize.STRING
      },
      createdTime: {
        allowNull: false,
        type: Sequelize.DATE
      },
      updatedTime: {
        allowNull: false,
        type: Sequelize.DATE
      },
      size: {
        type: Sequelize.INTEGER,
        defaultValue: 0,
        allowNull: false
      },
      filename: {
        type: Sequelize.STRING
      },
      resource: {
        type: Sequelize.STRING
      },
    });
  },
  down: (queryInterface, Sequelize) => {
    return queryInterface.dropTable('Files');
  }
};