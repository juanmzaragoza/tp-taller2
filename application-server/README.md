# Application Server

## Constantes
Existe el archivo de constantes [constants.py](constants.py)
Para utilizar una constante del mismo:
* Importar la constante: `from constants import SHARED_SERVER_URL`


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





