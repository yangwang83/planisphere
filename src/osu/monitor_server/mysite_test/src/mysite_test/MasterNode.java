package mysite_test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLEncoder;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MasterNode {
    static String NodeURL1 = "http://localhost:8082/node1";
    static String NodeURL2 = "http://localhost:8083/node2";
    static String WebserverURL = "http://localhost:8000/message/reponse_msg";
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
          HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
          server.createContext("/master", new MyHandler());
          server.start();
          System.out.println("Master begin to run!");
    }
    
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
            BufferedReader br = new BufferedReader(isr);

            // From now on, the right way of moving from bytes to utf-8 characters:

            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }

            br.close();
            isr.close();
            System.out.println(buf);
            
            
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            //System.out.println(response);
            os.close();
            
            String message = buf.toString();
            //redirect to the destination node
            String[] components = message.split(" ");
            //String content = components[2];
            
            if (Utility.isNormalMessage(message)) {
                //System.out.println("get the normal message");
                String parameters = developParameter(components);
                String webserverReponse = Utility.sendHttpRequest(WebserverURL, parameters).trim();
                
                System.out.println(webserverReponse);
                if (webserverReponse.equals("accepted")) {
                    System.out.println("the decision is accept");
                    //send message to destination and source
                    String source = components[0];
                    String destination = components[1];
                    if (destination.equals("a1")) {
                        Utility.sendHttpRequest(NodeURL1, buf.toString());
                        Utility.sendHttpRequest(NodeURL2, "message is accepted");
                    } else if (destination.equals("a2")) {
                        Utility.sendHttpRequest(NodeURL2, buf.toString());
                        Utility.sendHttpRequest(NodeURL1, "message is accepted");
                    }
                    
                } else if (webserverReponse.equals("dropped")){
                    System.out.println("the decision is dropped");
                    //send message to source
                    String source = components[0];
                    if (source.equals("a1")) {
                        Utility.sendHttpRequest(NodeURL1, "dropped the message");
                    } else if (source.equals("a2")) {
                        Utility.sendHttpRequest(NodeURL2, "dropped the message");
                    }
                } else if (webserverReponse.equals("killed")) {
                    System.out.println("the decision is kill the node");
                    //send message to source
                    String source = components[0];
                    if (source.equals("a1")) {
                        Utility.sendHttpRequest(NodeURL1, "kill the node");
                    } else if (source.equals("a2")) {
                        Utility.sendHttpRequest(NodeURL2, "kill the node");
                    }
                }
            }
            
            
        }
        
        public String developParameter(String[] components) throws UnsupportedEncodingException {
            String from = components[0];
            String to = components[1];
            String content = components[2];
            String time = components[3];
            String status = components[4];
            String urlParameters = 
                    "source=" + URLEncoder.encode(from, "UTF-8") +
                    "&destination=" + URLEncoder.encode(to, "UTF-8") +
                    "&content=" + URLEncoder.encode(content, "UTF-8") +
                    "&time=" + URLEncoder.encode(time, "UTF-8") +
                    "&status=" + URLEncoder.encode(status, "UTF-8");
            return urlParameters;
        }
    }

}
