class DataVersionException(Exception):
    def __init__(self):
        super(DataVersionException, self).__init__("Error: You are trying to update an outdated version")
