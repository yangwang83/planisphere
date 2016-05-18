package osu.planisphere;

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
	
	private boolean running = true;
	private LinkedBlockingQueue<Message> incomingQueue = 
			new LinkedBlockingQueue<Message>(Configuration.maxQueueSize);
	
	private NodeIdentifier id;
	private long timerInterval;
	private Network network;
	private EventHook hook = null;
	
	/**
	 * Constructor
	 * @param id: ID of this node
	 * @param timerInterval: the interval (milliseconds) to trigger timer
	 * @param network: the network class used to transfer messages
	 */
	public Node(NodeIdentifier id, int timerInterval, Network network){
		this.id = id;
		this.timerInterval = timerInterval;
		this.network = network;
		this.network.registerNode(id, this);
	}
	
	public NodeIdentifier getID(){
		return this.id;
	}
	
	/**
	 * A hook is mainly used to inject failures for testing.
	 * @param hook
	 */
	public void addEventHook(EventHook hook){
		this.hook = hook;
	}
	
	protected void enqueueMessage(Message msg){
		try{
			incomingQueue.put(msg);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		
		long lastTimerTime = System.currentTimeMillis();
		
		while(running){
			try{
				Message msg = incomingQueue.poll(timerInterval/10, TimeUnit.MILLISECONDS);
				if(msg != null){
					if(hook == null)
						handleMessage(msg);
					else
						hook.handleMessage(msg, this);
				}
				else{
					if(System.currentTimeMillis() - lastTimerTime > timerInterval){
						if(hook == null)
							handleTimer();
						else
							hook.handleTimer(this);
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
	public void sendMessage(NodeIdentifier receiver, Message msg){
		if(hook == null)
			this.sendMessageInternal(receiver, msg);
		else
			hook.sendMessage(receiver, msg, this);
	}
	
	public void sendMessageInternal(NodeIdentifier receiver, Message msg){
		this.network.sendMessage(receiver, msg);
	}

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
	public abstract void handleMessage(Message msg);
	
}
