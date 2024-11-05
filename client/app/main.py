import sys
import os

# Add the path to the sys.path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))) # This is the path to the parent directory

from app import Config
from server.flaskSetup import app

# GLOBAL VARIABLES
appName = "data analitics"
appDataBase = "sqlite:///db.sqlite3"
# True for development, False for production mode. 
# for get a new render template, keep it True. otherwise you will not see the changes upto you restart the server
deBugMode = True 

# Creating a new instance of the Config class
config = Config(appName, appDataBase, deBugMode)

if __name__ == '__main__':
    config.show_config()
    app.run(debug=deBugMode) # Run the app in debug mode