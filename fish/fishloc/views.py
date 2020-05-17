from .serializers import FishSerializer
from .forms import *
from .models import FishLocation, FishDetail

from django.http import HttpResponse
from django.shortcuts import render, redirect 

from rest_framework import generics

class FishLocationViewSet(generics.ListCreateAPIView):
    queryset = FishLocation.objects.all().order_by('name')
    serializer_class = FishSerializer

def index(request):
    locations = FishLocation.objects.all().order_by('name')
    details = FishDetail.objects.all().order_by('name')
    context = {
        'details': details,
        'locations': locations,
    }
    return render(request, 'fishloc/index.html', context)
  
# Create your views here. 
def fish_image_view(request): 
  
    if request.method == 'POST': 
        form = FishForm(request.POST, request.FILES) 
  
        if form.is_valid(): 
            form.save() 
            return redirect('success') 
    else: 
        form = FishForm() 
    return render(request, 'fishloc/fish_form.html', {'form' : form}) 
  
  
def success(request): 
    return HttpResponse('successfully uploaded') 