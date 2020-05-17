from django.contrib import admin

# Register your models here.
from .models import FishLocation, FishDetail

class FishLocationAdmin(admin.ModelAdmin):  # add this
    list_display = ('name', 'score')  # add this

admin.site.register(FishLocation, FishLocationAdmin)
admin.site.register(FishDetail)