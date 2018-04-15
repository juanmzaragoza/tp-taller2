# Conexion DB
Para conectarse a la DB dockerizada puede hacer utilizando el el psql desde dentro del container, o pgadmin4.

## psql
- Acceder al container con el comando: `docker exec -it 'postgresql' bash`
- Luego conectarse a la DB: `psql -h localhost -p 5432 -d postgres -U postgres --password`
- El password es: 0000

## PGAdmin4
- Instalación: https://medium.com/@philip.mutua/postgresql-install-pgadmin-4-desktop-mode-in-ubuntu-16-04-6faca19f0cfe
- Para la conexión son los mismos datos, excepto el host. Para el host tuve que fijarme la ip del container de postgresql usando `docker inspect`

