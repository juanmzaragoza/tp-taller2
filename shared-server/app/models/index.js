const Sequelize = require('sequelize');
// var config = require('config');
var env = process.env.NODE_ENV || 'development';
var config = require(__dirname + '/../../config/database.json')[env];

function loadDatabase() {
    console.log('Loading database...');
    
    // Create db connection
    const sequelize = new Sequelize(
        config.database,
        config.username,
        config.password,
        {
            host: config.host,
            port : config.port,
            dialect: config.dialect,
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
    require('./app_server')(Sequelize, sequelize, models);
    
    return {
        db: sequelize,
        models: models
    };
}

module.exports = {
    init: loadDatabase
};