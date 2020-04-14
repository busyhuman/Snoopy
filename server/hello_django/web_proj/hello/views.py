from django.shortcuts import render
from django.http import HttpResponse
import json
from collections import OrderedDict
from .models import User, Food #models에 정의된 Candidate를 불러온다

def home(request):
    food = Food.objects.all()

    file_data = OrderedDict()
    s = ""
    for foo in food:
        file_data["Num"] = foo.Num
        file_data["Food_Name"] = foo.Food_Name
        file_data["Kcal"] = foo.Kcal
        s += json.dumps(file_data, ensure_ascii=False, indent="\t")
        s += "<br>"
    return HttpResponse(s)

