import pymongo
import os

url = 'mongodb://localhost:27017'
client = pymongo.MongoClient(url, username=os.getenv('MONGO_USERNAME'), password=os.getenv('MONGO_PASSWORD'))

db = client['billings_db']