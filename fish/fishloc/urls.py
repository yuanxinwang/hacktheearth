from django.urls import path
from django.conf import settings 
from django.conf.urls.static import static 
from .views import *
from . import views


urlpatterns = [
    path('location/', views.FishLocationViewSet.as_view()),
    path('', views.index, name='index'),
    path('fish_form', fish_image_view, name = 'fish_form'), 
    path('success', success, name = 'success'), 
]

if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
else:
    urlpatterns += staticfiles_urlpatterns()