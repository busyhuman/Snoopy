import csv
import os
import django

os.environ.setdefault("DJANGO_SETTING_MODULE", "[web_proj].settings")
django.setup()

from .models import Food

CSV_PATH = '/Users/busyh/Desktop/foods.csv'

with open(CSV_PATH, newline='') as csvfile:
    data_reader = csv.DictReader(csvfile)
    for row in data_reader:
        Food.objects.create(
            Num = row[0],
            Food_Name = row[4],
            Category = row[8],
            Serving_Size = row[9],
            Kcal = row[11],
            Carbo = row[17],
            Protein = row[15],
            Fat = row[16],
            Natrium = row[38],
        )