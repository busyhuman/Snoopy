from django.db import models

class User(models.Model):
    objects = models.Manager()
    ID = models.TextField(primary_key=True, max_length=15)
    PW = models.TextField(max_length=15)
    UserName = models.TextField(max_length=5)
    Age = models.IntegerField(default=0)
    Sex = models.CharField(max_length=1)  # 1로 바꿈
    Height = models.FloatField(default=0)
    Weight = models.FloatField(default=0)
    Activity_Index = models.IntegerField(default=0)
    UserKcal = models.FloatField(default=0)

    def __str__(self):
        return self.ID


class Food(models.Model):
    objects = models.Manager()
    Num = models.IntegerField(primary_key=True)
    FoodName = models.TextField(max_length=15)
    Category = models.TextField(max_length=5)
    ServingSize = models.FloatField(default=0)
    Kcal = models.FloatField(default=0)
    Carbo = models.FloatField(default=0)
    Protein = models.FloatField(default=0)
    Fat = models.FloatField(default=0)
    Saccharide = models.FloatField(default=0)
    Natrium = models.FloatField(default=0)

    def __str__(self):
        return self.FoodName


class Bookmark(models.Model):
    objects = models.Manager()
    BMNum = models.AutoField(primary_key = True)
    FoodName = models.TextField(max_length=20)     # Food는 테이블명이라 바꿈
    user = models.ForeignKey(User, on_delete=models.CASCADE) # 유저가 삭제되면 같이 삭제
    FoodNum = models.IntegerField(default=0)

    def __str__(self):
        return self.FoodName


class Stats(models.Model):
    objects = models.Manager()
    StatsID = models.AutoField(primary_key = True)
    Date = models.DateTimeField()
    Carbo = models.FloatField(default=0)
    Protein = models.FloatField(default=0)
    Fat = models.FloatField(default=0)
    Saccharide = models.FloatField(default=0)
    Natrium = models.FloatField(default=0)
    user = models.ForeignKey(User, on_delete=models.CASCADE)    # 기본키 제거, 유저가 삭제되면 같이 삭제
    FoodFoodNum = models.IntegerField(default=0)  # 기본키 제거

    def __str__(self):
        return self.StatsID