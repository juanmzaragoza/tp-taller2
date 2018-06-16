class NoReactionFoundException(Exception):
    def __init__(self):
        super(NoReactionFoundException, self).__init__("Reaction not found")
