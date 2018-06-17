from constants import JWT_SECRET
import jwt
from jwt import ExpiredSignatureError
from flask import request
import flask
app = flask.Flask(__name__)

def get_token():
	token = _get_token()
	if (is_token_valid(token)):
		app.logger.error('es valido todavia')
	else: 
		app.logger.error('ya no es mas valido')			
	return is_token_valid(token)

def _get_token():
	authorization = request.headers.get('authorization')
	token = authorization.replace('Basic ','')
	return token

def is_token_valid(token):
	if (not token):
		return False
	try:
		payload = _decode_token(token)
		expiration = payload.get('exp')
		app.logger.error('expiration: %s', expiration)			
		return True
	except ExpiredSignatureError:
		return False

def _decode_token(token):
	payload = jwt.decode(token, JWT_SECRET)
	app.logger.error('payload: %s', payload)
	return payload

def _get_username(payload):
	data = payload.get('data')
	username = data.get('username')
	return username
