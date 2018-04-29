'use strict';
module.exports = {
  up: (queryInterface, Sequelize) => {
    return queryInterface.createTable('Users', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      username: {
        type: Sequelize.STRING,
        allowNull: false,
        unique: true
      },
      password: {
        type: Sequelize.STRING,
        allowNull: false
      },
      rev: {
        type: Sequelize.STRING
      },
      token: {
        type: Sequelize.STRING
      },
      role: {
        type: Sequelize.STRING
      },
      tokenFace: {
        type: Sequelize.STRING
      },
      type: {
        type: Sequelize.STRING
      },
      applicationOwner: {
        type: Sequelize.STRING
      }
    });
  },
  down: (queryInterface, Sequelize) => {
    return queryInterface.dropTable('Users');
  }
};