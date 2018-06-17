class NoFriendFoundException(Exception):
    def __init__(self):
        super(NoFriendFoundException, self).__init__("Friendship not found")
