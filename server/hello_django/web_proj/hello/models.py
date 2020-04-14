from django.db import models

class User(models.Model):
    objects = models.Manager()
    ID = models.TextField(primary_key=True, max_length=15)
    PW = models.TextField(max_length=15)
    User_Name = models.TextField(max_length=5)
    Age = models.IntegerField(default=0)
    Sex = models.CharField(max_length=1)  # 1로 바꿈
    Height = models.FloatField(default=0)
    Weight = models.FloatField(default=0)
    Activity_Index = models.IntegerField(default=0)
    User_Kcal = models.FloatField(default=0)

    def __str__(self):
        return self.ID


class Food(models.Model):
    objects = models.Manager()
    Num = models.IntegerField(primary_key=True)
    Food_Name = models.TextField(max_length=15)
    Category = models.TextField(max_length=5)
    Serving_Size = models.FloatField(default=0)
    Kcal = models.FloatField(default=0)
    Carbo = models.FloatField(default=0)
    Protein = models.FloatField(default=0)
    Fat = models.FloatField(default=0)
    Saccharide = models.FloatField(default=0)
    Natrium = models.FloatField(default=0)

    def __str__(self):
        return self.Food_Name


class Bookmark(models.Model):
    objects = models.Manager()
    BM_Num = models.IntegerField(primary_key=True, default=0)
    Food_Name = models.TextField(max_length=20)     # Food는 테이블명이라 바꿈
    User_ID = models.TextField(max_length=15)
    Food_Num = models.IntegerField(default=0)

    def __str__(self):
        return self.Food_Name


class Stats(models.Model):
    objects = models.Manager()
    Stats_ID = models.IntegerField(primary_key=True,default=0)
    Date = models.DateTimeField()
    Carbo = models.FloatField(default=0)
    Protein = models.FloatField(default=0)
    Fat = models.FloatField(default=0)
    Saccharide = models.FloatField(default=0)
    Natrium = models.FloatField(default=0)
    User_ID = models.IntegerField(default=0)    # 기본키 제거
    Food_Food_Num = models.IntegerField(default=0)  # 기본키 제거

    def __str__(self):
        return self.Stats_ID