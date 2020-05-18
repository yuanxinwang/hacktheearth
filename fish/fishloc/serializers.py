from rest_framework import serializers
from fishloc.models import FishLocation, FishDetail

class LocationSerializer(serializers.ModelSerializer):
    class Meta:
        model = FishLocation
        fields = ['name', 'lat', 'lon', 'score']

class DetailSerializer(serializers.ModelSerializer):
    class Meta:
        model = FishDetail
        fields = ['name', 'description', 'img']