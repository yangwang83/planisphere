from django.http import HttpResponse
import json
from datetime import datetime
import urllib2
import urllib
import datetime
from django.shortcuts import render
from models import *
from django.views.decorators.csrf import csrf_exempt

@csrf_exempt
def index(request):
    all_nodes = Nodes.objects.all()
    # initialize node database
    if len(all_nodes) == 0:
        new_node = Nodes(NodeID="a1", Name="a1", Status="living")
        new_node.save()
        new_node = Nodes(NodeID="a2", Name="a2", Status="living")
        new_node.save()
        new_node = Nodes(NodeID="a3", Name="a3", Status="living")
        new_node.save()
        new_node = Nodes(NodeID="a4", Name="a4", Status="living")
        new_node.save()
        all_nodes = Nodes.objects.all()

    messages = []
    nodes = []
    all_ms = Messages.objects.all()
    for ms in all_ms:
        m = {}
        m['source'] = ms.Source
        m['destination'] = ms.Destination
        m['content'] = ms.Content
        m['time'] = ms.Time
        m['status'] = ms.Status
        m['realtime'] = str(ms.RealTime)
        messages.append(m)
    for node in all_nodes:
        n = {}
        n['nodeid'] = node.NodeID
        n['name'] = node.Name
        n['status'] = node.Status
        nodes.append(n)
    return render(request, 'index.html', {'nodes':json.dumps(nodes), 'messages': json.dumps(messages)})

@csrf_exempt
def putSendMsgToDB(request):
    print(request.POST['msg'])
    context = {}
    if 'msg' in request.POST or not request.POST['msg']:
        try:
            msg = request.POST['msg']
            source, destination, content, time, status = msg.split(" ")
            new_msg = Messages(Source=source, Destination=destination, Content=content, Time=int(time), Status=status)
            new_msg.save()
            context['source'] = source
            context['destination'] = destination
            context['content'] = content
            context['time'] = time
            context['status'] = status
        except Exception:
            context['error'] = 'wrong msg.'
    else:
        context['error'] = 'no input message!'

    return render(request, 'index.json', context, content_type='application/json')

@csrf_exempt
def updateNodeInDB(request):
    nodeid = request.POST['nodeid']
    status = request.POST['status']
    nodes = Nodes.objects.filter(NodeID=nodeid)
    for n in nodes:
        n.Status = status
        n.save()
    context = {}
    return render(request, 'index.json', context, content_type='application/json')

@csrf_exempt
def updateSendMsgToDB(request):
    source = request.POST['from']
    destination = request.POST['to']
    timeStart = request.POST['timeStart']
    content = request.POST['content']
    status = request.POST['status']
    ms = Messages.objects.filter(Source = source, Destination = destination, Time = timeStart)
    for m in ms:
        m.Status = status
        m.save()

    context = {}

    context['source'] = source
    context['destination'] = destination
    context['content'] = content
    context['time'] = timeStart
    context['status'] = status

    return render(request, 'index.json', context, content_type='application/json')

@csrf_exempt
def intervalUpdateMsgToDB(request, time):
    if time == 'undefined':
        time = "1970-01-01T00:00+00:00"
    max_time = Messages.get_max_time()
    all_posts = Messages.get_changes(time)
    info = []
    res = dict()

    for post in all_posts:
        res = dict(post = post)
        info.append(res)
    context = {"max_time": max_time, 'posts': info}
    return render(request, 'posts.json', context, content_type='application/json')

@csrf_exempt
def clearDB(request):
    print "it will clear the message database"
    Messages.objects.all().delete()
    context = {}
    return render(request, 'index.json', context, content_type='application/json')

@csrf_exempt
def clearNodeDB(request):
    print "it will clear the node database"
    Nodes.objects.all().delete()
    context = {}
    return render(request, 'index.json', context, content_type='application/json')

@csrf_exempt
def testMsg(request):
    source = (request.POST['source'])
    destination = (request.POST['destination'])
    content = (request.POST['content'])
    time = (request.POST['time'])
    status = (request.POST['status'])
    new_msg = Messages(Source=source, Destination=destination, Content=content, Time=int(time), Status=status)
    new_msg.save()

    # sending the http requests to java server.
    # url = 'http://localhost:8081/test'
    # value = {'response':'accept'}
    # data = urllib.urlencode(value)
    # req = urllib2.Request(url, data)
    # f = urllib2.urlopen(req)
    # print f.read()

    return HttpResponse('Hello world')