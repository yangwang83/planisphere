# -*- coding: utf-8 -*-
# Generated by Django 1.11 on 2017-10-22 17:40
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('message', '0003_messages_realtime'),
    ]

    operations = [
        migrations.CreateModel(
            name='Nodes',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('NodeID', models.CharField(max_length=200)),
                ('Name', models.CharField(max_length=200)),
                ('Status', models.CharField(max_length=200)),
            ],
        ),
    ]
