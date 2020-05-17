from django import forms 
from .models import *
  
class FishForm(forms.ModelForm): 
  
    class Meta: 
        model = FishDetail 
        fields = ['name', 'description', 'img'] 