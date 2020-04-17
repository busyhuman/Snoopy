from django.conf.urls import url
from django.urls import path
from hello import views

urlpatterns = [
   path(
        'api/v1/users/', #1 
        views.UserCreateReadView.as_view(),
        name='user_rest_api' #2
        ),
   path(
        'api/v1/foods/', #3
        views.FoodAPIView.as_view(),
        name='food_rest_api' #4
    ),

]