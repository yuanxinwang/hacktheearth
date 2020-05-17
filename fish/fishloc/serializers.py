from rest_framework import serializers
from fishloc.models import FishLocation

class FishSerializer(serializers.ModelSerializer):
    class Meta:
        model = FishLocation
        fields = ['name', 'lat', 'lon', 'score']