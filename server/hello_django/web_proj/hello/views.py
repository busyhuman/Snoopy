from .models import User, Food 
from rest_framework import generics
from .serializers import UserSerializer, FoodSerializer
from django_filters import rest_framework, NumberFilter

from django.http import HttpResponse
import csv
import os
import django

class UserCreateReadView(generics.ListCreateAPIView):
    """
    queryset = User.objects.all()
    serializer_class = UserSerializer
    filter_backends = (rest_framework.DjangoFilterBackend, )
    filter_fields = ('ID',)
    """

class FoodAPIView(generics.ListCreateAPIView):    
    queryset = Food.objects.all()  
    serializer_class = FoodSerializer    
    filter_backends = (rest_framework.DjangoFilterBackend, )
    filter_fields = ('Num', )
