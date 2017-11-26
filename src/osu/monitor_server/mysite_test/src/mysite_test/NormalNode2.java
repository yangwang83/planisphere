package mysite_test;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class NormalNode2 {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        HttpServer server = HttpServer.create(new InetSocketAddress(8083), 0);
        server.createContext("/node2", new NormalNodeHandler());
        server.start();
        System.out.println("normalnode2 begin to run!");
    }

}
