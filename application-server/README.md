# Application Server

## Constantes
Existe el archivo de constantes [constants.py](constants.py)
Para utilizar una constante del mismo:
* Importar la constante: `from constants import SHARED_SERVER_URL`

## MONGODB
Para establecer la conexion a la base de datos, se debe indicar la URI a traves de una variable de entorno.  
* HEROKU `export MONGO_URI='mongodb://ds231199.mlab.com:31199'`  
* LOCAL  `export MONGO_URI='mongodb://localhost:27017'`  
* DOCKER `export MONGO_URI='mongodb://mongo:27017'`  

## Logging
Para loggear en flask:
* Importar Flask: `import flask`
* Acceder a la app: `app = flask.Flask(__name__)`
* Loggear con distinto niveles: `app.logger.error('url: %s', self.url)`

## Unit Tests

La idea es siempre utilizar en los entornos de desarrollo Docker, para evitar conflicto de versiones.

### Local
* Desde el directorio /test ejecutar: `pytest` 

### Docker
* Acceder al bash del container: `docker exec -it 'gunicorn' bash`
* Desde la raiz ejecutar: `python -m pytest test/`





