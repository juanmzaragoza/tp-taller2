const Sequelize = require('sequelize');
var config = require('config');

function loadDatabase() {
    console.log('Loading database...');
    
    // Create db connection
    const sequelize = new Sequelize(
        config.get('database.database'),
        config.get('database.username'),
        config.get('database.password'),
        {
            host: config.get('database.host'),
            port : config.get('database.port'),
            dialect: config.get('database.dialect'),
            define: {
                timestamps: false,
            },
            operatorsAliases: false,
            pool: {
                max: 5,
                min: 0,
                acquire: 30000,
                idle: 10000
            },
        }
    );
    sequelize.authenticate().then(() => {
        console.log('Connection has been established successfully.');
    }).catch(err => {
        console.error('Unable to connect to the database:', err);
    });

    var models = {};

    require('./user')(Sequelize, sequelize, models);
    
    return {
        db: sequelize,
        models: models
    };
}

module.exports = {
    init: loadDatabase
};