from .models import User, Food, Bookmark, Stats
from rest_framework import viewsets, generics
from .serializers import UserSerializer, FoodSerializer, BookMarkSerializer, StatsSerializer
from django_filters import rest_framework, NumberFilter

from django.http import HttpResponse
import csv
import os
import django

class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()  
    serializer_class = UserSerializer 
    filter_backends = (rest_framework.DjangoFilterBackend, )
    filter_fields = ('ID', 'UserName')

class FoodAPIView(generics.ListCreateAPIView):    
    queryset = Food.objects.all()  
    serializer_class = FoodSerializer    
    filter_backends = (rest_framework.DjangoFilterBackend, )
    filter_fields = ('Num', 'FoodName', 'Category', 'ServingSize', 'Kcal', 'Carbo', 'Protein', 'Fat', 'Natrium',)


class BookmarkViewSet(viewsets.ModelViewSet):
    queryset = Bookmark.objects.all()  
    serializer_class = BookMarkSerializer
    filter_backends = (rest_framework.DjangoFilterBackend, )
    filter_fields = ('BMNum', 'user', )

class StatsViewSet(viewsets.ModelViewSet):
    queryset = Stats.objects.all()  
    serializer_class = StatsSerializer
    filter_backends = (rest_framework.DjangoFilterBackend, )
    filter_fields = ('StatsID', 'user', 'FoodFoodNum', 'Date',)