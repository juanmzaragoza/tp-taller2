class DataVersionException(Exception):
    def __init__(self):
        super(DataVersionException, self).__init__("This version of this data is not compatible")
