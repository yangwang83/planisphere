package osu.planisphere;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A pseudo network to transfer messages among nodes
 * @author Yang Wang
 *
 */
public class Network {
	
	
	private ServerSocket ss = null;
	private ServerSocketThread ssThread = null;
	private Node node = null;
	private List<ReceiverThread> receiverThreads = new LinkedList<ReceiverThread>();
	private final HashMap<InetSocketAddress, Socket> outgoingSockets = new HashMap<InetSocketAddress, Socket>();
	
	
	/*
	 * Create the server socket and bind it to port.
	 * If port is -1, then bind to a random port.
	 * Return value: the actual port
	 */
	public void init(int port, Node node){
		try{
			this.node = node;
			ss = new ServerSocket();
			if(port>0)
				ss.bind(new InetSocketAddress("localhost", port));
			else
				ss.bind(null);
			ssThread = new ServerSocketThread(ss);
			ssThread.start();
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public int getLocalPort(){
		return ss.getLocalPort();
	}
	
	public void close(){
		for(ReceiverThread t : receiverThreads){
			t.terminate();
		}
		ssThread.terminate();
		for(Socket sock : outgoingSockets.values()){
			try {
				sock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class ServerSocketThread extends Thread{
		
		private ServerSocket ss = null;
		private boolean running = true;
		public ServerSocketThread(ServerSocket ss){
			this.ss = ss;
		}
		
		public synchronized void terminate(){
			running = true;
			this.interrupt();
			try{
				ss.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		
		@Override
		public void run(){
			while(true){
				synchronized(this){
					if(!running)
						return;
				}
				try{
					Socket sock = ss.accept();
					ReceiverThread t = new ReceiverThread(sock);
					receiverThreads.add(t);
					t.start();
				}
				catch(IOException e){
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	private class ReceiverThread extends Thread{
		
		private Socket sock = null;
		private boolean running = true;
		
		public synchronized void terminate(){
			running = true;
			this.interrupt();
			try{
				sock.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		
		public ReceiverThread(Socket sock){
			this.sock = sock;
		}
		
		public void run(){
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(sock.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			while(true){
				synchronized(this){
					if(!running)
						return;
				}
				try{
					Message msg = (Message)ois.readObject();
					node.enqueueMessage(msg);
				}
				catch(IOException e){
					e.printStackTrace();
					return;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	public void sendMessage(InetSocketAddress receiver, Message msg){
		Socket s = null;
		synchronized(outgoingSockets){
			if(outgoingSockets.containsKey(receiver))
				s = outgoingSockets.get(receiver);
			else{
				s = new Socket();
				try {
					s.connect(receiver);
					outgoingSockets.put(receiver, s);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					s = null;
				}
				
			}
		}
		if(s!=null){
			synchronized(s){
				try {
					ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
}
