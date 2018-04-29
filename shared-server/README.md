# Conexion DB
Para conectarse a la DB dockerizada puede hacer utilizando el el psql desde dentro del container, o pgadmin4.

## psql
- Acceder al container con el comando: `docker exec -it 'postgresql' bash`
- Luego conectarse a la DB: `psql -h localhost -p 5432 -d postgres -U postgres --password`
- El password es: 0000

## PGAdmin4
- Instalación: https://medium.com/@philip.mutua/postgresql-install-pgadmin-4-desktop-mode-in-ubuntu-16-04-6faca19f0cfe
- Para la conexión son los mismos datos, excepto el host. Para el host tuve que fijarme la ip del container de postgresql usando `docker inspect`

### Run PGAdmin
- cd ~/pgadmin4
- source bin/activate
- python lib/python2.7/site-packages/pgadmin4/pgAdmin4.py


# Migration
http://docs.sequelizejs.com/manual/tutorial/migrations.html
Para correr las migraciones y los seeds, basta con hacer
`docker-compose exec 'node' sh migrations.sh`

## Crear migrations
`node_modules/.bin/sequelize model:generate --name User --attributes firstName:string,lastName:string,email:string`

## Run migration
`node_modules/.bin/sequelize db:migrate`


## Undo migration

### La mas reciente
`node_modules/.bin/sequelize db:migrate:undo`

### Todas
`node_modules/.bin/sequelize db:migrate:undo:all`

### Hasta una migration especifica
`node_modules/.bin/sequelize db:migrate:undo:all --to XXXXXXXXXXXXXX-create-posts.js`


## Crear seed
`node_modules/.bin/sequelize seed:generate --name demo-user`


## Run seeds
`node_modules/.bin/sequelize db:seed:all`


## Undo seed

## La mas reciente
node_modules/.bin/sequelize db:seed:undo

## Todas
node_modules/.bin/sequelize db:seed:undo:all
