# Instalación Stories - Taller de Programacion II
Para instalar la última versión estable de nuestros sistemas procure estar ubicando en el branch `master`. Para ello, procure descargar el código y obtener esta última versión.

    git checkout master
    git pull origin master

## Requerimientos
* [docker-core](https://docs.docker.com/) (`17.05.0-ce, build 89658be`)
* [docker-compose](https://docs.docker.com/compose/) (`1.16.1, build 6d1ac21`)

## Instalación

### Servidores y web
Para levantar un entorno de desarrollo totalmente funcional es suficiente con ejecutar dentro de la carpeta `/docker`:

    docker-compose up --build
    
Para más información y detaller ver [`docker/README.md`](docker/README.md).

### Android
La aplicación Android requiere un tratamiento especial debido a que debe instalarse en cada smartphonee particularmente.

Para esto, dirijase a la carpeta `android-app/release/` y copie el archivo `stories-v1.apk` en su telefono a un directorio donde pueda ejecutarlo.

Luego, dirijase a **Menu > Settings > Security >** y tilde **Unknown Sources** para permitir instalar aplicaciones desde fuentes desconocidas (que no están en Google Play Store).

Por último, instale la APK en su telefóno.

## Depliegue en Heroku
La instalación productiva de los servidores se puede realizar siguiendo los pasos de [DEPLOY.md](DEPLOY.md)