# Docker TP 75.52 Taller de Programacion II
Contenedores e imagenes especializadas para el TP de 75.52 Taller de Programacion II.

Este documento contiene toda la informacion necesaria para levantar los contenedores necesarios para obtener  un entorno de desarrollo totalmente funcional. 

## Requerimientos
* [docker-core](https://docs.docker.com/) (`17.05.0-ce, build 89658be`)
* [docker-compose](https://docs.docker.com/compose/) (`1.16.1, build 6d1ac21`)

## Especificaciones
Este proyecto extiende las imagenes que se detallan a continuación para implementar los contenedores menconados.
* node (`8.10.0`)
* postgres (`10.3`)
* gunicorn (`3.6.4`)
* mongo (`3.2`)
* teracy/angular-cli (`latest`)
* heroku (`ubuntu:16.04`)
* swaggerui (`latest`)

### Herramienta utilizada
Para generar los contenedores de Dcoker utilizamos la herramienta Compose que nos permite a través de un YAML definir múltiples contenedores que pueden ser creados y con levantados con un solo comando.

En el archivo `docker-compose.yml` encontaremos los tags node, postgresql, web, gunicorn, mongo, heroku, swaggerui que instanciarán los contenededores de cada uno de los componentes necesarios para el desarrollo.

*Nota: tener en cuenta los puertos que se mapean con nuestro host para evitar problemas al levantar los contenedores. Por ej.:*  

    ports:
      - 81:8080

*implica que nuestro host mapea su puerto 81 con el puerto 8080 del container.*    

## Instalar contenedores
Si todavia no se encuentra dentro de la carpeta docker:

	cd PROJECT_ROOT/docker

Crear la carpeta `data/mongo` y `data/postgresql` que almacenara la informacion de las bases de datos

Ejecutar (la primera vez que inicializamos el proyecto):

	docker-compose rm --all 
	docker-compose build --no-cache

Luego:
	
	docker-compose up --build

Para acceder a algún contendedor, por ejemplo ´node´, una vez levantado el docker-compose ejecutar:

	docker exec -it 'node' bash

## Informacion adicional
Sobre los directorios:

  * `data`: guarda las BD creadas en los contenedores de PostreSQL y MongoDB.

  * `logs`: guarda los logs de las diferentes configuraciones

  * `node`: configuracion del contenedor de node

  * `gunicorn`: configuracion del contenedor gunicorn

### Comandos utiles
`docker-compose up` [Lee archivo docker-compose.yml e incia los servicios configurados]
`docker-compose build` [Lee Archivo docker-compose.yml Y RECONFIGURA los servicios en caso de haber algun cambio].
`docker ps` [lista los contenedores iniciados]
`docker ps -a` [lista todos los contenedores]
`docker stop 'nombre_or_id_container'` [detiene un contenedor o varios]
`docker stop $(docker ps -a -q)` [detiene todos los contenedores que se estan ejecutando]
`docker start 'nombre_or_id_container'` [inicia un contenedor o varios]
`docker restart 'nombre_or_id_container'` [reinicia un contenedor o varios]
`docker rm 'nombre_or_id_container'` [Elimina un contenedor]
`docker rm $(docker ps -a -q)` [Elimina todos los contenedores]
`docker rmi 'nombre_or_id_imagen'` [Elimina una imagen]
`docker rmi $(docker images -q)` [Elimina todas las imagenes]
`docker exec -it 'nombre_or_id_container' bash` [entras al contenedor]
`docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' container_name_or_id"` [Muestra IP de un contenedor]
`ping nombre_or_id_container` [cmando dentro de algun contendor para saber la ip de este].

## Troubleshooting
```ERROR: Couldn't connect to Docker daemon at http+docker://localunixsocket - is it running?

If it's at a non-standard location, specify the URL with the DOCKER_HOST environment variable.```

Al usar docker-compose -> correr con sudo