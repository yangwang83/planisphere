package osu.planisphere;

import java.io.ObjectOutputStream;
import java.net.Socket;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * This is the base class for all nodes that and
 * sending and processing messages.
 * 
 * If you need to implement your own specific kind
 * of nodes (e.g. client, acceptor, etc), you should
 * create a class extending this class. You will need to
 * implement the handleMessage and handleTimer functions.
 * On the other hand, you can use the sendMessage call
 * to send a message to another node.
 * 
 * To simplify design, this class keeps everything single-threaded,
 * so you don't need to worry about synchronization. However, this
 * means no functions or data structure are thread-safe: if you need
 * to create other threads inside a node, it is your responsibility
 * to make sure everything is synchronized properly.
 * @author Yang Wang
 *
 */
public abstract class Node extends Thread{
	
	private static class MessageAndOut{
		public Message msg;
		public ObjectOutputStream out;
		
		public MessageAndOut(Message msg, ObjectOutputStream out) {
			this.msg = msg;
			this.out = out;
		}
	}
	
	private boolean running = true;
	private LinkedBlockingQueue<MessageAndOut> incomingQueue = 
			new LinkedBlockingQueue<MessageAndOut>(Configuration.maxQueueSize);
	
	private NodeIdentifier id;
	private long timerInterval;
	
	/**
	 * Constructor
	 * @param id: ID of this node
	 * @param timerInterval: the interval (milliseconds) to trigger timer
	 * @param network: the network class used to transfer messages
	 */
	public Node(NodeIdentifier id, int timerInterval){
		this.id = id;
		this.timerInterval = timerInterval;

	}
	
	public NodeIdentifier getID(){
		return this.id;
	}
	
	
	protected void enqueueMessage(Message msg, ObjectOutputStream out){
		try{
			incomingQueue.put(new MessageAndOut(msg, out));
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		
		long lastTimerTime = System.currentTimeMillis();
		System.out.println("thread started");
		while(running){
			try{
				MessageAndOut msg = incomingQueue.poll(timerInterval/10, TimeUnit.MILLISECONDS);
				if(msg != null){
					handleMessage(msg.msg, msg.out);
				}
				else{
					if(System.currentTimeMillis() - lastTimerTime > timerInterval){
						handleTimer();
						lastTimerTime = System.currentTimeMillis();
					}
				}
			}
			catch(InterruptedException e){
				e.printStackTrace();
				break;
			}
		}
	}
	
	/**
	 * Send a message to receiver
	 * @param receiver: the id of the receiver
	 * @param msg: the message to be sent
	 */
	public abstract void sendMessage(NodeIdentifier receiver, Message msg);

	/**
	 * This function will be called periodically.
	 * The interval is determined by timerInterval in constructor.
	 * This function should be used to check any timeout.
	 * Note: handleTimer will not be called concurrently
	 * with handleMessage.
	 */
	public abstract void handleTimer();
	
	/**
	 * This function will be called when the node
	 * receives a message.
	 * Note: handleMessage will not be called concurrently
	 * with handleTimer.
	 * @param msg: the received message
	 */
	public abstract void handleMessage(Message msg, ObjectOutputStream out);
	
}
