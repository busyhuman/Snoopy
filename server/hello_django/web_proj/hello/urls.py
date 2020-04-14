from django.conf.urls import url
from hello import views

urlpatterns = [
   url(r'^$', views.home),  # 빈경로에서 views.home을 실행시켜라
]