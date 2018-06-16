class NoFriendRequestFoundException(Exception):
    def __init__(self):
        super(NoFriendRequestFoundException, self).__init__("Friend Request not found")
