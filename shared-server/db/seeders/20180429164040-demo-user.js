'use strict';

module.exports = {
    up: (queryInterface, Sequelize) => {
        return queryInterface.bulkInsert('Users', [{
            username: "erikschmoll", 
            role: "admin", 
            password: "$2b$04$X/1z1pL/h1IjCrGhHSWxYOA9tMe8EabIkJsW7eHk6CsccS4M0dnPi"
        },
        {
            username: "juanmzaragoza", 
            role: "admin", 
            password: "$2b$04$X/1z1pL/h1IjCrGhHSWxYOA9tMe8EabIkJsW7eHk6CsccS4M0dnPi"
        },
        {
            username: "nicolas7885", 
            role: "admin", 
            password: "$2b$04$X/1z1pL/h1IjCrGhHSWxYOA9tMe8EabIkJsW7eHk6CsccS4M0dnPi"
        },
        {
            username: "mevk", 
            role: "admin", 
            password: "$2b$04$X/1z1pL/h1IjCrGhHSWxYOA9tMe8EabIkJsW7eHk6CsccS4M0dnPi"
        },
        {
            username: "kaoruheanna", 
            role: "admin", 
            password: "$2b$10$doWoE5cZhen.bXA8lCVzA.BSXQm/NexmHGNUNJ/mZOHX/AMfsOrv." //1234
        },
        {
            username: "test", 
            role: "app", 
            password: "$2b$10$doWoE5cZhen.bXA8lCVzA.BSXQm/NexmHGNUNJ/mZOHX/AMfsOrv." //1234
        },
        {
            username: "test2", 
            role: "app", 
            password: "$2b$10$dTOh/HW8xqCH.JdhwFeuwOCpNlZHourvNmLSJN3qUp5PJgqLdgpM6" //0000
        },
        {
            username: "test3", 
            role: "app", 
            password: "$2b$10$2sM4uYHxpCk8x6KGzSqhuOSNSJj3fTUeNNIOG2Ia.ayW9SHoRlt36" //password
        }], {});
    },

    down: (queryInterface, Sequelize) => {
        return queryInterface.bulkDelete('Users', null, {});
    }
};
