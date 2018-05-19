class DataAlreadyExistsException(Exception):
    def __init__(self):
        super(DataAlreadyExistsException, self).__init__("Data already exists")
