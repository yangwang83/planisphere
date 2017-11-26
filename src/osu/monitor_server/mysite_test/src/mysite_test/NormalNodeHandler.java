package mysite_test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class NormalNodeHandler implements HttpHandler{
    String MasterURL = "http://localhost:8081/master";
    String previousMessage = "";
    @Override
    public void handle(HttpExchange t) throws IOException {
        // TODO Auto-generated method stub
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
        if (Utility.isNormalMessage(message)) {
            System.out.println("reponse the received message");
            //response to the message
            String[] components = buf.toString().split(" ");
            String content = components[2];
            String reponseContent = 
                    components[1] + " " + components[0] + " " + "hi" + " " + String.valueOf(Integer.valueOf(components[3]) + 1) + " " + "pending";
            previousMessage = reponseContent;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Utility.sendHttpRequest(MasterURL, reponseContent);
        } else if (message.equals("dropped the message")){
            //resend the previous message
            try {
                System.out.println("try to resend the previous message");
                Thread.sleep(1000);
                String[] preComponents = previousMessage.split(" ");
                String preContent = 
                        preComponents[0] + " " + preComponents[1] + " " + "hi" + " " + String.valueOf(Integer.valueOf(preComponents[3]) + 1) + " " + "pending";
                previousMessage = preContent;
                Utility.sendHttpRequest(MasterURL, preContent);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (message.equals("kill the node")) {
            System.out.println("this node is killed");
            System.exit(0);
        }
    }
}
