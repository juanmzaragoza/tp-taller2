from functools import wraps

import jwt
from flask import request
from jwt import ExpiredSignatureError, DecodeError

from constants import JWT_SECRET
from controllers.error_handler import ErrorHandler
from errors_exceptions.user_mismatch_exception import UserMismatchException
from models.user_activity import UserActivityModel


def is_authenticated():
	token = _get_token()
	is_valid = _is_token_valid(token)
	if (is_valid):
		_save_user_activity()
	return is_valid

def _get_token():
	authorization = request.headers.get('authorization')
	if (not authorization):
		return None
	token = authorization.replace('Basic ','')
	return token

def _is_token_valid(token):
	if (not token):
		return False
	payload = _decode_token(token)
	return True

def _decode_token(token):
	payload = jwt.decode(token, JWT_SECRET)
	return payload

def get_user_id():
	token = _get_token()
	if (not _is_token_valid(token)):
		return None
	payload = _decode_token(token)
	data = payload.get('data')
	user_id = data.get('userId')
	return user_id

def _save_user_activity():
	user_id = get_user_id()
	#UserActivityModel.update_user_activiy(user_id)
	UserActivityModel.log_user_login_activity(user_id)

def login_required(f):
	@wraps(f)
	def decorated_function(*args, **kwargs):
		try:
			if (not is_authenticated()):
				return ErrorHandler.create_error_response('invalid_token', 401)
			return f(*args, **kwargs)
		except DecodeError:
			return ErrorHandler.create_error_response('invalid_token', 401)
		except ExpiredSignatureError:
			return ErrorHandler.create_error_response('token-expired', 401)

	return decorated_function

def validate_sender(sender_id):
	user_id = get_user_id()
	if (int(user_id) != int(sender_id)):
		raise UserMismatchException()

