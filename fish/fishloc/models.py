from django.db import models

# Create your models here.
class FishLocation(models.Model):
    name = models.CharField(max_length=100, blank=True, default='')
    lat = models.FloatField()
    lon = models.FloatField()
    score = models.FloatField()

class FishDetail(models.Model):
    name = models.CharField(max_length=100, blank=True, default='')
    description = models.TextField(max_length=400, blank=True, default='')
    img = models.ImageField(upload_to='images/', default='')
