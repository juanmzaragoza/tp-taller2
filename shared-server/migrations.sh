#!/bin/bash
echo "Running Migrations"
node_modules/.bin/sequelize db:migrate

echo "Running Seeds"
node_modules/.bin/sequelize db:seed:all