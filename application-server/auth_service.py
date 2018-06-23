from functools import wraps
from controllers.error_handler import ErrorHandler
from constants import JWT_SECRET
import jwt
from jwt import ExpiredSignatureError
from flask import request
from models.user_activity import UserActivityModel

def is_authenticated():
	token = _get_token()
	is_valid = _is_token_valid(token)
	if (is_valid):
		_save_user_activity()
	return is_valid

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
        if (not is_authenticated()):
        	return ErrorHandler.create_error_response('token-expired', 401)
        return f(*args, **kwargs)
    return decorated_function

