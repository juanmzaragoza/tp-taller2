# Instalación Stories - Taller de Programacion II
Para instalar la última versión estable de nuestros sistemas procure estar ubicando en el branch `master`. Para ello, procure descargar el código y obtener esta última versión.

    git checkout master
    git pull origin master

## Requerimientos
* [docker-core](https://docs.docker.com/) (`17.05.0-ce, build 89658be`)
* [docker-compose](https://docs.docker.com/compose/) (`1.16.1, build 6d1ac21`)

## Instalación
Para levantar un entorno de desarrollo totalmente funcional es suficiente con ejecutar dentro de la carpeta `/docker`:

    docker-compose up --build
    
Para más información y detaller ver `docker/README.md`.

## Depliegue en Heroku
La instalación productiva se puede realizar siguiendo los pasos de [DEPLOY.md](DEPLOY.md)