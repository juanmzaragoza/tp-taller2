# Deployment Stories - Taller de Programacion II

## Subir a Heroku [Primera vez]

Para realizar nuestro primer deploy de la aplicacion

### Deployment manual [Primera vez]

#### Paso 1: Configurar el cli de Heroku.

Instalar Heroku

```bash
sudo add-apt-repository "deb https://cli-assets.heroku.com/branches/stable/apt ./"
curl -L https://cli-assets.heroku.com/apt/release.key | sudo apt-key add -
sudo apt-get update
sudo apt-get install heroku
```

##### Heroku accounts

Una utilidad interesante para tener varias cuentas en una misma máquina es `heroku-accounts`

```bash
heroku plugins:install heroku-accounts
```

Primero agregamos una cuenta

```bash
heroku accounts:add personal
```

Para listar y setear cuentas, hacemos respectivamente:

```bash
heroku accounts
heroku accounts:set personal
```

Por último, para saber que cuenta estamos usando actualmente

```bash
heroku accounts:current
```

#### Paso 2: Subir shared-server al cloud

Loguearse en la aplicación ingresando las credenciales del shared-server

```bash
heroku login
```

o bien, seteando el usuario del shared-server

```bash
heroku accounts:add shared-server
heroku accounts:set shared-server
```

y subir el código a Heroku

```bash
heroku create heroku-sharedserver
git subtree push --prefix shared-server heroku-sharedserver master
```

De este modo estaremos deployando la carpeta `/shared-server` al master de heroku que se pueda consultar ejecutando

```bash
heroku open --remote heroku-sharedserver
```

cuyos logs se pueden ver haciendo

```bash
heroku logs --remote heroku-sharedserver --tail
```

##### Primer deploy

Configurar el addon de postgresql para Heroku.
Corroborar que no esta instalado

```bash
	heroku addons --remote heroku-sharedserver
```

Agregar el addon

```bash
heroku addons:create heroku-postgresql:hobby-dev --remote heroku-sharedserver
```

Ver la informacion del mismo

```bash
heroku pg:credentials:url DATABASE --remote heroku-sharedserver
```

Instalar psql en el localhost para poder acceder a la consola desde nuestra maquina.
Para conectarse al posgresql:

```bash
heroku pg:psql --remote heroku-sharedserver
```

###### Subir un dump de nuestra base

Generar el dump en nuestro local

```bash
PGPASSWORD=mypassword pg_dump -Fc --no-acl --no-owner -h localhost -U myuser mydb mydb.dump
```

e importarlo en el postgresql de Heroku

```bash
heroku pg:backups:restore 'url' DATABASE_URL
```

donde `url` es el archivo que subimos a una carpeta publicada en internet y accesible desde heroku (dropbox link por ejemplo).

Por ultimo, entrar a la configuracion de Heroku https://dashboard.heroku.com/apps/heroku-sharedserver/settings y configurar las variables de entorno: DATABASE_PORT, DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_HOST. Estos valores se obtienen obteniendo la informacion de postresql en la nube

```bash
heroku pg:credentials:url DATABASE --remote heroku-sharedserver
```

#### Paso 3: Subir application-server al cloud

Loguearse en la aplicación ingresando las credenciales del application-server

```bash
heroku login
```

o bien, seteando el usuario del application-server

```bash
heroku accounts:add application-server
heroku accounts:set application-server
```

y subir el código a la nube

```bash
heroku create heroku-applicationserver
git subtree push --prefix application-server heroku-applicationserver master
```

De este modo estaremos deployando la carpeta `/shared-server` al master de heroku que se pueda consultar ejecutando

```bash
heroku open --remote heroku-applicationserver
```

cuyos logs se pueden ver haciendo

```bash
heroku logs --remote heroku-applicationserver --tail
```

#### Paso 4: Subir frontend al cloud

Loguearse en la aplicación ingresando las credenciales del frontend

```bash
heroku login
```

o bien, seteando el usuario del frontend

```bash
heroku accounts:add web
heroku accounts:set web
```

y subir el código a la nube

```bash
heroku create heroku-storiesweb
git subtree push --prefix web heroku-storiesweb master
```

De este modo estaremos deployando la carpeta `/web` al master de heroku que se pueda consultar ejecutando

```bash
heroku open --remote heroku-storiesweb
```

cuyos logs se pueden ver haciendo

```bash
heroku logs --remote heroku-storiesweb --tail
```

### Deployment en un paso

Para evitar realizar el deploy manual, es conveniente ejecutar un script que realiza los pasos anteriores automáticamente.

#### Paso 1: Permisos a deploy.sh.

Dar permisos de ejecución a `deploy.sh`

```bash
chmod +x deploy.sh
```

#### Paso 2: Agregar cuentas de Heroku.

De antemano hay que agregar cuentas utilizando `Heroku accounts`. Ver Paso 1 de la instalación manual.

#### Paso 3: Ejecutar scripts.

Ejecutar

```bash
./deploy.sh
```