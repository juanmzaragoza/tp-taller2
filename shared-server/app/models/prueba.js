'use strict';
module.exports = (sequelize, DataTypes) => {
  var Prueba = sequelize.define('Prueba', {
    firstName: DataTypes.STRING,
    lastName: DataTypes.STRING,
    email: DataTypes.STRING
  }, {});
  Prueba.associate = function(models) {
    // associations can be defined here
  };
  return Prueba;
};