from django.conf.urls import url, include
from django.urls import path
from hello import views
from rest_framework import routers
from .views import UserViewSet, BookmarkViewSet, StatsViewSet

router = routers.DefaultRouter()
router.register('users', UserViewSet)
router.register('bookmarks', BookmarkViewSet)
router.register('stats', StatsViewSet)


urlpatterns = [
     path('', include(router.urls)),
     path(
          'foods/', #3
          views.FoodAPIView.as_view(),
          name='food_rest_api' #4
          ),
]