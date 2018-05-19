class NoDataFoundException(Exception):
    def __init__(self):
        super(NoDataFoundException, self).__init__("No Data Found")
