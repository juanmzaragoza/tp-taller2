const Sequelize = require('sequelize');
var env = process.env.NODE_ENV || 'development';
var config = require(__dirname + '/../../config/database.json')[env];

function loadDatabase() {
    console.log('Loading database...');
    
    // Create db connection
    const sequelize = new Sequelize(
        process.env.DATABASE_NAME || config.database,
        process.env.DATABASE_USER || config.username,
        process.env.DATABASE_PASSWORD || config.password,
        {
            host: process.env.DATABASE_HOST || config.host,
            port : process.env.DATABASE_PORT ||config.port,
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
    require('./file')(Sequelize, sequelize, models);
    
    return {
        db: sequelize,
        models: models
    };
}

module.exports = {
    init: loadDatabase
};