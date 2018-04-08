# Instalación Stories - Taller de Programacion II

## Requerimientos

En ubuntu:
	-npm
	-pip3
	-node 8.10
	-python 3.6

## Instalación

android app:
	-cd android-app/ && ./gradlew build jacocoTestReport assembleAndroidTest

shared server:
	- cd shared-server/ && npm install

application server:
	- cd application-server/ && pip3 install -r requirements.txt

Por el momento, para desarrollo, ver `docker/README.md`.

## Pruebas

android app:
	- cd android-app/ && ./gradlew test

shared server:
	- cd shared-server/ && npm test

application server:
	-cd application-server/ && test
