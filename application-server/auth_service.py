from functools import wraps
from controllers.error_handler import ErrorHandler
from constants import JWT_SECRET
import jwt
from jwt import ExpiredSignatureError
from flask import request
import flask
app = flask.Flask(__name__)

def is_authenticated():
	token = _get_token()
	return _is_token_valid(token)

def _get_token():
	authorization = request.headers.get('authorization')
	token = authorization.replace('Basic ','')
	return token

def _is_token_valid(token):
	if (not token):
		return False
	try:
		payload = _decode_token(token)
		return True
	except ExpiredSignatureError:
		return False

def _decode_token(token):
	payload = jwt.decode(token, JWT_SECRET)
	# app.logger.error('payload: %s', payload)
	return payload

def _get_username(payload):
	data = payload.get('data')
	username = data.get('username')
	return username

def login_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if (not is_authenticated()):
        	return ErrorHandler.create_error_response('token-expired', 401)
        return f(*args, **kwargs)
    return decorated_function

