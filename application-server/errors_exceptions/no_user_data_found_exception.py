class NoUserDataFoundException(Exception):
    def __init__(self):
        super(NoUserDataFoundException, self).__init__("User not found")
