let startPosX = 100;
let svg;
let currentNodeNum = 0;
let nodeInfo = {};

let currentTimeEnd = 10;
let intervalLength = 50;
let currentSvgWidth = 500;

let forklength = 20;
let maxTime = '';

//extend the lines of nodes if necessary
function updateWidth(timeEnd){
  if(timeEnd <= currentTimeEnd)
    return;
  currentTimeEnd = timeEnd;
  currentSvgWidth = timeEnd * intervalLength;
  d3.select("#mainSvg").style("width", currentSvgWidth+500);

  d3.selectAll(".myline").each(function() {
    d3.select(this).attr("x2", startPosX +currentSvgWidth );
  })
}

//initial the basic setting
function initScene() {
  svg = d3.select("#mainSvg")
    .style("width", currentSvgWidth+startPosX+50)
    .style("height",0);

  defs = svg.append("defs")

  defs.append("marker")
    .attr({
      "id":"arrow",
      "viewBox":"0 -5 10 10",
      "refX":5,
      "refY":0,
      "markerWidth":4,
      "markerHeight":4,
      "orient":"auto"
    })
    .append("path")
      .attr("d", "M0,-5L10,0L0,5")
      .attr("class","arrowHead");

  $("#dialog").dialog({
    autoOpen: false
  });

  maxTime = '';
}

//draw the specific node
function drawEle(x, y, i) {
  currentNodeNum += 1;
  let currentY = 100 + (currentNodeNum-1)*200

  let circR = 20;
  svg.style("height", parseInt(svg.style("height"))+200);
  svg.append('circle')
    .attr({
      cx:startPosX - circR,
      cy:currentY,
      r:circR
    });

  nodeInfo[i] = {y: currentY};

  svg.append('line')
    .attr({
      "y1":currentY ,
        "y2":currentY ,
        "x1":startPosX,
        "x2":startPosX+currentSvgWidth,
        "class": "myline"
      })

  svg.append('text').
  attr({
    x:startPosX,
    y:currentY,
    r:20,
    "font-size":'20px',
    fill:'red'
      }).text(""+i)
}

//get the position of x according to time
function getTimePosX(time) {
  return time * intervalLength + startPosX;
}

//get the arrow id
function getID(from, to, start) {
  return "arrow-"+from+"-"+to+"-"+start;
}

//draw arrow according to messages
function drawArrow(from, to, timeStart, content, status, interval) {
  if(!interval)
    interval = 1;

  timeEnd = +timeStart + interval;

  updateWidth(timeEnd)

  if (status == "pending") {
    line_id = getID(from, to, timeStart)
    svg.append('line')
       .attr({
         "class":"arrow",
         "id": line_id,
         "marker-end":"url(#arrow)",
         "x1":getTimePosX(timeStart),
         "y1":nodeInfo[from].y,
         "x2":getTimePosX(timeEnd),
         "y2":nodeInfo[to].y,
         "from": from,
         "to": to,
         "start": timeStart,
         "end": timeEnd,
         "content": content,
         "status": status
       })
      .on('click', function(){
        let from = d3.select(this).attr("from")
        let to = d3.select(this).attr("to")
        let timeStart = d3.select(this).attr("start")
        let content = d3.select(this).attr("content")
        let status = d3.select(this).attr("status")
        
        //create a dialog obejct
        openDialog(from, to, timeStart, content, status);
        })
      .style("cursor",'pointer');

  } else if (status == "accepted" || status == "dropped") {
      svg.append('line')
       .attr({
         "class":"solid_arrow",
         "id": getID(from, to, timeStart),
         "marker-end":"url(#arrow)",
         "x1":getTimePosX(timeStart),
         "y1":nodeInfo[from].y,
         "x2":getTimePosX(timeEnd),
         "y2":nodeInfo[to].y,
         "from": from,
         "to": to,
         "start": timeStart,
         "end": timeEnd,
         "content": content,
         "status": status
       })
    if (status == "dropped") {
      drawFork(from, to, timeStart, interval);
    }
  }
}

//if we drop a message, we draw fork in the middle of the message
function drawFork(from, to, timeStart, interval) {
  if (!interval)
    interval = 1;

  center_x = (getTimePosX(timeStart) + getTimePosX(timeEnd)) / 2;
  center_y = (nodeInfo[to].y + nodeInfo[from].y) / 2;

  x1 = center_x - forklength / 2
  y1 = center_y + forklength / 2
  x2 = center_x + forklength / 2
  y2 = center_y - forklength / 2 

  timeEnd = +timeStart + interval;
  svg.append('line')
     .attr({
       "class":"solid_fork",
       "x1":x1,
       "y1":y1,
       "x2":x2,
       "y2":y2,
  })

  svg.append('line')
     .attr({
       "class":"solid_fork",
       "x1":x1,
       "y1":y2,
       "x2":x2,
       "y2":y1,
  })
}

//draw the fork for the killed node
function drawForkForNode(node) {
  console.log(node)
  center_x = startPosX - 20
  center_y = nodeInfo[node].y
  console.log(center_x)
  console.log(center_y)

  x1 = center_x - forklength / 2
  y1 = center_y + forklength / 2
  x2 = center_x + forklength / 2
  y2 = center_y - forklength / 2 

  svg.append('line')
   .attr({
     "class":"solid_fork",
     "x1":x1,
     "y1":y1,
     "x2":x2,
     "y2":y2,
  })

  svg.append('line')
     .attr({
       "class":"solid_fork",
       "x1":x1,
       "y1":y2,
       "x2":x2,
       "y2":y1,
  })
}

var ginfo = {}

//click on the arrow, it will show dialog
function openDialog(from, to, timeStart, content, status) {
  let message = from + " send '" + content + "' to " + to + " at time:" + timeStart

  console.log("1 --------------" + message);
  $("#dialog").dialog("open");
  $('.dialog_title').html(message);

  ginfo.from = from;
  ginfo.to = to;
  ginfo.timeStart = timeStart;
  ginfo.content = content;
}

//kill the node and update node information in the server
function updateNode(nodeid, status) {
  context = {}
  context['nodeid'] = nodeid
  context['status'] = status
  console.log(nodeid)
  console.log(status)
  $.ajax({
    url: "/message/update_node", 
    data: context,
    type: "POST",
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    },
    success: function(res) {
      drawForkForNode(nodeid)
    },
    error: function(e, status) {
      console.log(status);
      console.log(e);
    },
    async: true,
  });
}

//update the message according user's decision
function updateMessage(from, to, timeStart, content, status) {
  let message = from + " send '" + content + "' to " + to + " at time:" + timeStart + " / status:" + status
  console.log("updateMessage: " + message)
  context = {}
  context['from'] = from;
  context['to'] = to;
  context['timeStart'] = timeStart;
  context['content'] = content;
  context['status'] = status;

  $.ajax({
    url: "/message/update_msg", 
    data: context,
    type: "POST",
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    },
    success: function(res) {
        let from = res.source;
        let to = res.destination;
        let content = res.content;
        let time = res.time;
        let status = res.status;
        drawArrow(from , to, time, content, status);
    },
    error: function(e, status) {
      console.log(status);
      console.log(e);
    },
    async: true,
  });
}

//clear the message in the web server
function clearMessage() {
  content = {}
  maxTime = '';
  $.ajax({
    url: "/message/clear_msg", 
    data: content,
    type: "POST",
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    },
    success: function(res) {
      window.location.reload();
    },
    error: function(e, status) {
      console.log(status);
      console.log(e);
      
    },
    async: true,
  });
}

//clear the node information in the web server
function clearNode() {
  content = {}
  maxTime = '';
  $.ajax({
    url: "/message/clear_node", 
    data: content,
    type: "POST",
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    },
    success: function(res) {
      window.location.reload();
    },
    error: function(e, status) {
      console.log(status);
      console.log(e);
    },
    async: true,
  });
}

//clear the messages in the database
function firmClearMessage() {
  if(confirm("Are you sure to clear the messages?")) {
    clearMessage();
  }
}

//clear the nodes information in the database and initial the node information from server
function firmClearNode() {
  if(confirm("Are you sure to clear the nodes information?")) {
    clearNode();
  }
}

//confirm dialog about kill node
function firmKillNode(from) {
  if(confirm("Are you sure to kill the sending node?")) {
    updateNode(from, "killed");
    $("#dialog").dialog("close");
  }
}

//the message status if database of messages update
function IntervalUpdateMessage() {
    if(typeof maxTime == 'undefined' || maxTime=='')  {
      maxTime = "1970-01-01T00:00+00:00";
    }
    content = {}
    //send the request messages to query whether the database update
    $.ajax({
      url: "/message/interval_update_msg/" + maxTime, 
      data: content,
      type: "POST",
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
      },
      success: function(res) {
        maxTime = res['max-time']
        console.log(maxTime);
        console.log(res.posts.length);
        for (var i = 0; i < res.posts.length; i++) {
          post = res.posts[i];

          from = post['post_source']
          to = post['post_destination']
          content = post['post_content']
          time = post['post_time']
          status = post['post_status']
          console.log(from)
          console.log(to)
          console.log(content)
          console.log(time)
          console.log(status)
          drawArrow(from , to, time, content, status);
        }
      },
      error: function(e, status) {
        console.log(status);
        console.log(e);
      },
      async: true,
    });
  }

$( document ).ready(function() { 
  initScene();
  for (index in nodes) {
    node = nodes[index]
    nodeid = node['nodeid']
    name = node['name']
    status = node['status']

    drawEle(0, y0, nodeid)
    if (status == 'killed') {
      drawForkForNode(nodeid);
    }
    console.log(nodeid)
  }

  if(typeof maxTime == 'undefined' || maxTime=='')  {
    maxTime = "1970-01-01T00:00+00:00";
  }

  for (index in messages) {
    message = messages[index]
    from = message['source']
    to = message['destination']
    content = message['content']
    time = message['time']
    status = message['status']
    realtime = message['realtime']
    if (realtime > maxTime) {
      maxTime = realtime
    }


    drawArrow(from , to, time, content, status);
  }
  
  //keeping the track of all changes in the database
  //window.setInterval(IntervalUpdateMessage, 500);


  $('#accept_msg').click(function() {
    if(ginfo.from === undefined)
      return;
    console.log("called accepted message!!!!!");
    $("#dialog").dialog("close");
    updateMessage(ginfo.from, ginfo.to, ginfo.timeStart, ginfo.content, "accepted")
    ginfo = {};
  });
  
  $('#drop_msg').click(function() {
    console.log("called dropped message!!!!!");
    $("#dialog").dialog("close");
    updateMessage(ginfo.from, ginfo.to, ginfo.timeStart, ginfo.content, "dropped")
  });
  
  $('#kill_node').click(function() {
    firmKillNode(ginfo.from);
  });

  $('#post-form').on('submit', function(event){

      event.preventDefault();
      var content = $(this).serializeArray();
    $.ajax({
      url: "/message/send_msg", 
      data: content,
      type: "POST",
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
      },
      success: function(res) {
          let from = res.source;
          let to = res.destination;
          let content = res.content;
          let time = res.time;
          let status = res.status
          drawArrow(from , to, time, content, status);
      },
      error: function(e, status) {
        console.log(status);
        console.log(e);
        
      },
      async: true,
    });
  })

  // CSRF set-up copied from Django docs
  function getCookie(name) {
    var cookieValue = null;
    if (document.cookie && document.cookie !== '') {
        var cookies = document.cookie.split(';');
        for (var i = 0; i < cookies.length; i++) {
            var cookie = jQuery.trim(cookies[i]);
            // Does this cookie string begin with the name we want?
            if (cookie.substring(0, name.length + 1) === (name + '=')) {
                cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return cookieValue;
  }

  var csrftoken = getCookie('csrftoken');

  function csrfSafeMethod(method) {
    // these HTTP methods do not require CSRF protection
    return (/^(GET|HEAD|OPTIONS|TRACE)$/.test(method));
  }

  $.ajaxSetup({
      beforeSend: function(xhr, settings) {
          if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
              xhr.setRequestHeader("X-CSRFToken", csrftoken);
          }
      }
  });
});
