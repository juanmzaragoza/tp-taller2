class FriendshipAlreadyExistsException(Exception):
    def __init__(self):
        super(FriendshipAlreadyExistsException, self).__init__("Friendship already exists")
