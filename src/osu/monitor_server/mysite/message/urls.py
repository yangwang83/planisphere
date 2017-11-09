from django.conf.urls import include, url
from . import views


urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^send_msg$', views.putSendMsgToDB, name='send'),
    url(r'^update_msg$', views.updateSendMsgToDB, name='update'),
    url(r'^update_node$', views.updateNodeInDB, name='updatenode'),
    url(r'^clear_msg$', views.clearDB, name='clear'),
    url(r'^clear_node$', views.clearNodeDB, name='clearnode'),
    url(r'^test_msg$', views.testMsg, name='test'),
    url(r'^interval_update_msg/(?P<time>.+)$', views.intervalUpdateMsgToDB, name='interval')
]