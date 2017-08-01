package osu.planisphere;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

import osu.planisphere.messages.GetRegistryMessage;
import osu.planisphere.messages.GetRegistryReplyMessage;
import osu.planisphere.messages.RegisterMessage;

public abstract class NormalNode extends Node {

	private Network network;
	private Socket masterSocket;
	private ObjectOutputStream oos;
	ObjectInputStream ois;
	private final HashMap<NodeIdentifier, InetSocketAddress> nodes = new HashMap<NodeIdentifier, InetSocketAddress>();
	
	public NormalNode(NodeIdentifier id, int timerInterval) {
		super(id, timerInterval);
		
		this.network = new Network();
		this.network.init(-1, this);
		
		try {
			masterSocket = new Socket();
			masterSocket.connect(Configuration.masterAddr);
			
			oos = new ObjectOutputStream(masterSocket.getOutputStream());
			oos.writeObject(new RegisterMessage(this.getID(), this.network.getLocalAddress()));
			
			ois = new ObjectInputStream(masterSocket.getInputStream());
			System.out.println(this.getID()+" sent a registry");
			
			
		}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		this.start();
	}
	
	public abstract void handleMessage(Message msg);
	
	public void handleMessage(Message msg, ObjectOutputStream out) {
		handleMessage(msg);
	}

	
	@Override
	public void sendMessage(NodeIdentifier receiver, Message msg) {
		InetSocketAddress target = null;
		synchronized(nodes) {
			if(nodes.containsKey(receiver)) {
				target = nodes.get(receiver);
			}
		}
		if(target == null) {
			
			try {
				oos.writeObject(new GetRegistryMessage(this.getID(), receiver));
				GetRegistryReplyMessage reply = (GetRegistryReplyMessage) ois.readObject();
				target = reply.getAddress();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized(nodes) {
				nodes.put(receiver, target);
			}
		}
		network.sendMessage(target, msg);

	}

}
