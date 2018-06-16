class NoCommentFoundException(Exception):
    def __init__(self):
        super(NoCommentFoundException, self).__init__("Comment not found")
