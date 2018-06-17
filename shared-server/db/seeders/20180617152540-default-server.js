'use strict';

module.exports = {
    up: (queryInterface, Sequelize) => {
        return queryInterface.bulkInsert('App_servers', [{
            name: 'Stories',
            rev: '123456789',
            createdTime: '2018-06-03 18:00:42.95+00',
            userId: 1,
            token: 'defaultToken'
    }], {});

    },

    down: (queryInterface, Sequelize) => {
    /*
      Add reverting commands here.
      Return a promise to correctly handle asynchronicity.

      Example:
      return queryInterface.bulkDelete('Person', null, {});
    */
    }
};
