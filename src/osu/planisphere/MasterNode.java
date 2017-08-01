package osu.planisphere;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

import osu.planisphere.messages.*;

public class MasterNode extends Node {
	
	private HashMap<NodeIdentifier, InetSocketAddress> nodes = new HashMap<NodeIdentifier, InetSocketAddress> ();
	private Network network;

	public MasterNode(NodeIdentifier id) {
		super(id, 5000);
		this.network = new Network();
		this.network.init(Configuration.masterAddr.getPort(), this);
		this.start();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void sendMessage(NodeIdentifier receiver, Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleTimer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMessage(Message msg, ObjectOutputStream out) {
		System.out.println("master got "+msg);
		if(msg instanceof RegisterMessage) {
			RegisterMessage rMsg = (RegisterMessage) msg;
			nodes.put(rMsg.getSender(), rMsg.getAddress());
		}
		else if(msg instanceof GetRegistryMessage) {
			GetRegistryMessage gMsg = (GetRegistryMessage)msg;
			if(!nodes.containsKey(gMsg.getTarget())) {
				try {
					out.writeObject(new GetRegistryReplyMessage(this.getID(), gMsg.getTarget(), null));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				InetSocketAddress addr = nodes.get(gMsg.getTarget());
				ObjectOutputStream oos;
				try {
					out.writeObject(new GetRegistryReplyMessage(this.getID(), gMsg.getTarget(), addr));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}

	}

}
