class FriendRequestAlreadyExistsException(Exception):
    def __init__(self):
        super(FriendRequestAlreadyExistsException, self).__init__("Friend request already exists")
