
# OBJECTS CONTAINING THE CONFIGURATION OF THE APP
class Config:
    def __init__(server, appName = "",  dbUri = "", debug = bool):
        server.app_name = appName
        server.db_uri = dbUri
        server.debug = debug

    def show_config(self):
        print('App Name:', self.app_name)
        print('Debug:', self.debug)
        print('DB URI:', self.db_uri)

# EXPORTING THE CONFIGURATION OBJECTS
config = Config()
