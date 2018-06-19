class StorieReactionAlreadyFoundException(Exception):
    def __init__(self):
        super(StorieReactionAlreadyFoundException, self).__init__("Storie Reaction already exists for this user")
