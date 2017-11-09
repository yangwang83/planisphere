package mysite_test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ConnectionTest {
    static String targetURL = "http://localhost:8000/message/test_msg";

    public static void main(String[] args) throws IOException, InterruptedException {
        //deploy the httpserver for handle the request sent from Django server
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/test", new MyHandler());
        server.start();
        
        //SendURL("a1","a2","haha","1", "pending");
        
        //send some messages information to the Django server
        String[] nodes = {"a1","a2","a3","a4"};
        Random rand = new Random();
        for(int i = 0; i < 4; i++) {
            String from = nodes[rand.nextInt(nodes.length)];
            String to = nodes[rand.nextInt(nodes.length)];
            if(from.equals(to)) {
                continue;
            }
            SendURL(from,to,"haha",String.valueOf(i), "pending");
        }
    }
    
    //This is the function build the parameters
    public static String SendURL(String from, String to, String content, String time, String status) throws UnsupportedEncodingException, InterruptedException {
        String urlParameters = 
                "source=" + URLEncoder.encode(from, "UTF-8") +
                "&destination=" + URLEncoder.encode(to, "UTF-8") +
                "&content=" + URLEncoder.encode(content, "UTF-8") +
                "&time=" + URLEncoder.encode(time, "UTF-8") +
                "&status=" + URLEncoder.encode(status, "UTF-8");
        String result = executePost(targetURL, urlParameters);
        Thread.sleep(1000);
        return result;
    }
    
    //this myhandler deal with the http request sent from Django server
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
        }
    }
    
    //this function is sending the messages to the Django database
    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
          //Create connection
          URL url = new URL(targetURL);
          connection = (HttpURLConnection) url.openConnection();
          connection.setRequestMethod("POST");
          connection.setRequestProperty("Content-Type", 
              "application/x-www-form-urlencoded");

          connection.setRequestProperty("Content-Length", 
              Integer.toString(urlParameters.getBytes().length));
          connection.setRequestProperty("Content-Language", "en-US");  

          connection.setUseCaches(false);
          connection.setDoOutput(true);

          //Send request
          DataOutputStream wr = new DataOutputStream (
              connection.getOutputStream());
          wr.writeBytes(urlParameters);
          wr.close();

          //Get Response  
          InputStream is = connection.getInputStream();
          BufferedReader rd = new BufferedReader(new InputStreamReader(is));
          StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
          String line;
          while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
          }
          rd.close();
          return response.toString();
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        } finally {
          if (connection != null) {
            connection.disconnect();
          }
        }
      }
}