# Stories - Taller de Programacion II


[![Build Status](https://travis-ci.org/juanmzaragoza/tp-taller2.svg?branch=master)](https://travis-ci.org/juanmzaragoza/tp-taller2.svg?branch=master)
[![codecov](https://codecov.io/gh/juanmzaragoza/tp-taller2/branch/master/graph/badge.svg)](https://codecov.io/gh/juanmzaragoza/tp-taller2)

## Introducción
Una importante empresa AppMaker© ha decidido encargarnos el desarrollo de una red social. El principal objetivo es permitir conectar en tiempo real a personas de todo el mundo que deseen compartir experiencias.
AppMaker busca posicionarse en el mercado de aplicaciones, busca conectar esta aplicación con sus otras aplicaciones que poseen gran cantidad de usuarios.

## Especificaciones
La aplicaicón desarrollada consta de los siguientes componentes:
* Un servidor (Application Server), el cual será responsable de conectar a los usuarios.
  * Lenguaje: [Python 3](https://www.python.org/) (`3.6.4`)
  * Web Framework: [Flask](http://flask.pocoo.org/) (`0.12.2`)
  * WSGI HTTP Server: [Gunicorn](http://gunicorn.org/) (`19.7.1`)
  * Database: [MongoDB](https://www.mongodb.com/)  (`3.2`)
* Un servidor (Shared Server), el cual es responsable de la administración de los application servers, autenticación de usuarios y administración de archivos multimedia.
  * Lenguaje: [NodeJS](https://nodejs.org/) (`8.10.0`)
  * Web Framework: [ExpressJS](https://expressjs.com) (`4.16.3`)
  * Database: [PostgreSQL](https://www.postgresql.org/) (`10.3`)
* Un backend web que utiliza los servicios ofrecidos por el shared-server para administración.
  * Libreria Frontend: [Angular](https://angular.io/)  (`5.2.0`)
* Un cliente Android, el cual será utilizado por los usuarios.
  * API SDK Version 21

## Arquitectura
### Componentes
![Componentes](https://raw.githubusercontent.com/taller-de-programacion-2/taller-de-programacion-2.github.io/master/trabajo-practico/enunciados/2018/1/images/diagrama.png)

### Estructura de directrios
La estructura de directorios nos servirá para identificar los distintos módulos de nuestra aplicación.
* `/docker` - contiene los archivos que crean los contenedores necesarios para levantar el proyecto
* `/aplication-server` - contiene los archivos que implementan el Application Server
* `/shared-server` - contiene los archivos que implementan el Shared Server
* `/web` - contiene los archivos que implementan el sitio Web

## Instalación
Nuestra aplicación puede ser instalada tanto en un entorno local, para el desarrollo de nuevas funcionalidades, como deployarse/instalarse en un entorno productivo como heroku para que pueda ser accedida por los usuario finales.
Si  embargo, la aplicación Android solo posee una release productiva.

### Instalación en desarrollo
La instalación en desarrollo implica levantar docker para los servidores e instalar una APK en nuestro smartphone Android.
Ver [INSTALL.md](INSTALL.md)

### Depliegue en Heroku
La instalación productiva se puede realizar siguiendo los pasos de [DEPLOY.md](DEPLOY.md)