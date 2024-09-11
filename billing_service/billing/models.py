from django.db import models
from utils import db
from django.conf import settings
import os
from dotenv import load_dotenv


billings_collection = db['billings']