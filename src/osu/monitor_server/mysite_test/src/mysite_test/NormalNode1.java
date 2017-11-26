package mysite_test;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class NormalNode1 {
    static String MasterURL = "http://localhost:8081/master";
    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO Auto-generated method stub
      HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
      server.createContext("/node1", new NormalNodeHandler());
      server.start();
      System.out.println("normalnode1 begin to run!");
      
      Utility.sendHttpRequest(MasterURL, "a1 a2 hi 0 pending");
    }
}
