Run the Django server
1.enter the mysite directory which contains manage.py
2.input "python manage.py runserver" in terminal

If there are some database problem
1.input "python manage.py makemigrations message" in terminal
2.input "python manage.py migrate" in terminal
3.Then input "python manage.py runserver" in terminal

Open the webpage
1.open the web browser and input http://localhost:8000/message/

The buttons in the webpage
1.send
This button is used to send the message information to the database
the input form should be a string which follows the format "from to message time status". for example, "a1 a2 haha 2 pending". This is the function used for testing.

2.clear the messages
All sent messages are recorded in the database on the server. If you want to clear the message database, just click this button.

3.clear the nodes
All nodes information is recorded in the database on the server. If you want to clear the node database, click this button.

There are also some examples of communication of Django server and java server with HTTP requests.

testMsg in views.py is the example which receives the messages from java server and injects them into the server database. I also write the python example script which sends the HTTP requests to the java server.

ConnectionTest.java is the example of java server for communicating with Django server.