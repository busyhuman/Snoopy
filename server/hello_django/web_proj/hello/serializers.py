from rest_framework import serializers
from .models import User, Food

class UserSerializer(serializers.ModelSerializer):
    class Meta : 
        model = User
        fields = ('ID',
                  'UserName',)

class FoodSerializer(serializers.ModelSerializer):
    class Meta : 
        model = Food
        fields = ('Num', 
                  'FoodName',
                  'Kcal',)