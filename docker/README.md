Docker TP 75.52 Taller de Programacion II 
=========================================

Contenedores e imagenes especializadas para el TP de 75.52 Taller de Programacion II.

Este documento contiene toda la informacion necesaria para levantar los contenedores necesarios para obtener  un entorno de desarrollo totalmente funcional. 

Requerimientos
----------------------------------
docker-core
docker-compose

Especificaciones
----------------------------------
node 8.10.0
postgres 10.3

Instalar contenedores
----------------------------------
Si todavia no se encuentra dentro de la carpeta docker:

	cd PROJECT_ROOT/docker

Crear la carpeta `data/mongo` y `data/postgresql` que almacenara la informacion de las bases de datos

Ejecutar (la primera vez que inicializamos el proyecto):

	docker-compose rm --all 
	docker-compose build --no-cache

Luego:
	
	docker-compose up -d

Para acceder a algún contendedor, por ejemplo ´node´, ejecutar:

	docker exec -it 'node' bash

Informacion adicional
----------------------------------
Sobre los directorios:

  * data: guarda las BD creadas en los contenedores de PostreSQL y MongoDB.

  * logs : guarda los logs de las diferentes configuraciones

  * node: configuracion del contenedor de node

Comandos utiles
----------------------------------
docker-compose up [Lee archivo docker-compose.yml e incia los servicios configurados]
docker-compose build [Lee Archivo docker-compose.yml Y RECONFIGURA los servicios en caso de haber algun cambio].
docker ps [lista los contenedores iniciados]
docker ps -a [lista todos los contenedores]
docker stop 'nombre_or_id_container' [detiene un contenedor o varios]
docker stop $(docker ps -a -q) [detiene todos los contenedores que se estan ejecutando]
docker start 'nombre_or_id_container' [inicia un contenedor o varios]
docker restart 'nombre_or_id_container' [reinicia un contenedor o varios]
docker rm 'nombre_or_id_container' [Elimina un contenedor]
docker rm $(docker ps -a -q) [Elimina todos los contenedores]
docker rmi 'nombre_or_id_imagen' [Elimina una imagen]
docker rmi $(docker images -q) [Elimina todas las imagenes]
docker exec -it 'nombre_or_id_container' bash [entras al contenedor]
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' container_name_or_id" [Muestra IP de un contenedor]
ping nombre_or_id_container [cmando dentro de algun contendor para saber la ip de este].