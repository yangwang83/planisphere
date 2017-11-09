from django.db import models
from django.db.models import Max

class Messages(models.Model):
    id = models.AutoField(primary_key=True)
    Source = models.CharField(max_length=200)
    Destination = models.CharField(max_length=200)
    Content = models.CharField(max_length=520000)
    Time = models.IntegerField(default=1)
    Status = models.CharField(max_length=200)
    RealTime = models.DateTimeField(auto_now=True)

    @staticmethod
    def get_changes(time):
        return Messages.objects.filter(RealTime__gt=time).order_by('-RealTime')

    @staticmethod
    def get_max_time():
        return Messages.objects.all().aggregate(Max('RealTime'))['RealTime__max'] or "1970-01-01T00:00+00:00"

class Nodes(models.Model):
    NodeID = models.CharField(max_length=200)
    Name = models.CharField(max_length=200)
    Status = models.CharField(max_length=200)
