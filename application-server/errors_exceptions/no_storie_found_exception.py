class NoStorieFoundException(Exception):
    def __init__(self):
        super(NoStorieFoundException, self).__init__("Storie not found")
