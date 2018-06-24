class UserMismatchException(Exception):
    def __init__(self):
        super(UserMismatchException, self).__init__("Token user doesn't match with the request user")
